package com.elikill58.api.data.hook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.elikill58.api.PlayerData;
import com.elikill58.api.data.AbstractData;
import com.elikill58.api.data.Data;
import com.elikill58.api.game.GameProvider;

public class DatabaseData extends AbstractData {

	private final String username, password;
	private final String ip, database, table;
	private Connection connection;
	
	public DatabaseData(GameProvider pl) {
		super(pl);
		ConfigurationSection section = pl.getConfig().getConfigurationSection("data.database");
		this.ip = section.getString("ip");
		this.database = section.getString("database");
		this.table = section.getString("table");
		this.username = section.getString("user");
		this.password = section.getString("password");
		connect();
		try {
			String values = "";
			for(Data data : pl.getGame().dataValues) {
				values += data.toDB() + ",";
			}
			PreparedStatement state = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + table + "`"
					+ "( `id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY UNIQUE,"
					+ "`uuid` varchar(36) NOT NULL,"
					+ "`name` varchar(36) NOT NULL,"
					+ "`coins` double DEFAULT '0' NOT NULL,"
					+ values
					+ "`created_at` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL);");
			state.executeUpdate();
			state.close();
			List<Data> sorted = new ArrayList<>(pl.getGame().dataValues);
			sorted.sort((a, b) -> b.getDataVersion() - a.getDataVersion());
			for(Data data : sorted) {
				PreparedStatement checkColumnState = connection.prepareStatement("IF COL_LENGTH ('" + table + "'.'" + data.getKey() + "') IS NULL"
						+ "BEGIN"
						+ "	ALTER TABLE " + table
						+ "	 ADD " + data.toDB()
						+ values
						+ "END;");
				int returnedId = checkColumnState.executeUpdate();
				checkColumnState.close();
				if(returnedId == 0)
					break;
				else
					pl.getLogger().info("Edited database for column " + data.getKey() + ". Code: " + returnedId);
			}
		} catch (SQLException e) {
			pl.getLogger().severe("Failed to load database table ...");
			e.printStackTrace();
		}
	}
	
	@Override
	public PlayerData loadData(UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			PlayerData pi = null;
			try {
				PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE uuid = ?");
				statement.setString(1, uuid.toString());
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					double coins = resultSet.getDouble("coins");
					pi = new PlayerData(uuid, coins, resultSet.getString("name"));
					for(Data data : getPlugin().getGame().dataValues)
						pi.set(data.getKey(), resultSet.getObject(data.getKey()));
				} else {
					String playerName = Bukkit.getPlayer(uuid).getName();
					PreparedStatement statementCreate = getConnection()
							.prepareStatement("INSERT INTO " + table + " (uuid,name) VALUES (?,?)");
					statementCreate.setString(1, uuid.toString());
					statementCreate.setString(2, playerName);
					statementCreate.executeUpdate();
					pi = new PlayerData(uuid, 0, playerName);
				}
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pi;
		}).join();
	}

	@Override
	public void saveData(PlayerData pi) {
		CompletableFuture.runAsync(() -> {
			try {
				HashMap<Integer, Object> hash = new HashMap<>();
				int i = 3;
				String values = "";
				for(Data data : getPlugin().getGame().dataValues) {
					hash.put(i++, pi.get(data.getKey()));
					values += ", " + data.getKey() + " = ?";
				}
				
				PreparedStatement stateUpdate = getConnection()
						.prepareStatement("UPDATE " + table + " SET name = ?, coins = ?" + values + " WHERE uuid = ?");
				stateUpdate.setString(1, pi.getPlayerName());
				stateUpdate.setDouble(2, pi.getCoins());
				hash.forEach((nb, obj) -> {
					try {
						stateUpdate.setObject(nb, obj);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
				stateUpdate.setString(i, pi.getUUID().toString());
				stateUpdate.executeUpdate();
				stateUpdate.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Connection getConnection() {
		try {
			if(connection == null || connection.isClosed() || !connection.isValid(200))
				connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + "/" + database + "?autoReconnect=true", username, password);
			getPlugin().getLogger().info("Connected to database " + ip + "/" + database + " !");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		try {
			if(connection != null && !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
