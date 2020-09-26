package com.elikill58.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.elikill58.api.data.DataManager;
import com.elikill58.api.json.JSONObject;
import com.elikill58.api.json.parser.JSONParser;

public class PlayerData {

	private static final HashMap<UUID, PlayerData> DATA = new HashMap<>();
	private final UUID uuid;
    private final String playerName;
	private final HashMap<String, Object> content = new HashMap<>();
	private final HashMap<String, Integer> shop = new HashMap<>();
	private double coins = 0;
	
	public PlayerData(UUID uuid, double coins, String name) {
		this.uuid = uuid;
		this.coins = coins;
		this.playerName = name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public UUID getUUID() {
		return uuid;
	}

	public double getCoins() {
		return coins;
	}

	public void setCoins(double coins) {
		this.coins = coins;
		DataManager.getData().saveData(this);
	}

	public void addCoins(double add) {
		setCoins(coins + add);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) content.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T defaultValue) {
		return (T) content.computeIfAbsent(key, (keys) -> defaultValue);
	}
	
	public void increment(String key) {
		add(key, 1);
	}
	
	public void add(String key, int toAdd) {
		set(key, get(key, 0) + toAdd);
	}
	
	public void set(String key, Object obj) {
		content.put(key, obj);
	}
	
	public HashMap<String, Integer> getShop() {
		return shop;
	}
	
	public int getShopLevel(String name) {
		return shop.getOrDefault(name, 0);
	}
	
	public void addShop(String name, int i) {
		shop.put(name, i);
	}
	
	public void setShop(String name, int i) {
		shop.put(name, i);
	}
	
	public String getShopToJson() {
		return JSONObject.toJSONString(shop);
	}
	
	@SuppressWarnings("unchecked")
	public void fromJsonShop(String content) {
		try {
			((JSONObject) new JSONParser().parse(content)).forEach((name, i) -> shop.put((String) name, (Integer) i));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		DataManager.getData().saveData(this);
	}

	public static PlayerData getPlayerData(Player p) {
    	return getPlayerData(p.getUniqueId());
    }

	public static PlayerData getPlayerData(UUID uuid) {
    	return DATA.computeIfAbsent(uuid, (id) -> DataManager.getData().loadData(id));
    }
	
	public static List<PlayerData> getPlayerDatas(){
		return new ArrayList<>(DATA.values());
	}
}
