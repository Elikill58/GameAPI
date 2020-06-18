package com.elikill58.api.game;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public interface GameProvider {
	
	public Logger getLogger();
	
	public Plugin getPlugin();
	
	public FileConfiguration getConfig();
	
	public Game<?> getGame();
}
