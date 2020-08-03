package com.elikill58.api.data.hook;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.configuration.file.YamlConfiguration;

import com.elikill58.api.PlayerData;
import com.elikill58.api.data.AbstractData;
import com.elikill58.api.data.Data;
import com.elikill58.api.game.GameProvider;

public class FileData extends AbstractData {

	private final File DATA_FOLDER;
	
	public FileData(GameProvider pl) {
		super(pl);
		DATA_FOLDER = new File(pl.getPlugin().getDataFolder(), "user");
		if(!DATA_FOLDER.exists())
			DATA_FOLDER.mkdirs();
	}

	@Override
	public PlayerData loadData(UUID uuid) {
		return CompletableFuture.supplyAsync(() -> {
			PlayerData pd = null;
			try {
				YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DATA_FOLDER, uuid.toString() + ".yml"));
				pd = new PlayerData(uuid, config.getDouble("coins", 0), config.getString("playername"));
				for(Data data : this.getPlugin().getGame().dataValues)
					pd.set(data.getKey(), config.get(data.getKey()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pd;
		}).join();
	}

	@Override
	public void saveData(PlayerData sp) {
		CompletableFuture.runAsync(() -> {
			try {
				File configFile = new File(DATA_FOLDER, sp.getUUID().toString() + ".yml");
				YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
				config.set("playername", sp.getPlayerName());
				config.set("coins", sp.getCoins());
				for(Data data : this.getPlugin().getGame().dataValues)
					config.set(data.getKey(), sp.get(data.getKey()));
				config.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
