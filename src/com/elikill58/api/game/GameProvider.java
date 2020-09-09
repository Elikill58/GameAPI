package com.elikill58.api.game;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameProvider extends JavaPlugin {
	
	public abstract Game<?> getGame();
}
