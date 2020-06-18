package com.elikill58.api.data;

import java.util.UUID;

import com.elikill58.api.PlayerData;
import com.elikill58.api.game.GameProvider;

public abstract class AbstractData {

	private final GameProvider pl;
	
	public AbstractData(GameProvider pl) {
		this.pl = pl;
	}
	
	public GameProvider getPlugin() {
		return pl;
	}
	
	public abstract PlayerData loadData(UUID uuid);

	public abstract void saveData(PlayerData sp);
	
	public void close() {}
}
