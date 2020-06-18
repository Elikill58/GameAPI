package com.elikill58.api.data;

import com.elikill58.api.data.hook.DatabaseData;
import com.elikill58.api.data.hook.FileData;
import com.elikill58.api.game.GameProvider;

public class DataManager {

	private static AbstractData data;
	
	public static AbstractData getData() {
		return data;
	}
	
	public static void init(GameProvider pl) {
		if(pl == null || data != null)
			return;
		String provider = pl.getConfig().getString("data.provider", "file");
		if(provider.equalsIgnoreCase("database")) {
			pl.getLogger().info("Enabling database system ...");
			data = new DatabaseData(pl);
		} else if(provider.equalsIgnoreCase("file")) {
			pl.getLogger().info("Enabling file system ...");
			data = new FileData(pl);
		} else {
			pl.getLogger().warning("Unknow data provider: " + provider + ", loading default provider: file.");
			data = new FileData(pl);
		}
	}
}
