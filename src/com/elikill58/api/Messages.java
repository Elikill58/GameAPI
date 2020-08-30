package com.elikill58.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.elikill58.api.game.GameAPI;
import com.elikill58.api.utils.Utils;

public class Messages {

	private static YamlConfiguration config;
	private static File configFile;
	
	public static String getLiteMessage(String dir, Object... placeholders) {
		if(!config.contains(dir)) {
			GameAPI.GAME_PROVIDER.getLogger().info("Cannot find the message '" + dir + "'.");
			return dir;
		}
		String message = ChatColor.RESET + config.getString(dir);
		for (int index = 0; index <= placeholders.length - 1; index += 2)
			message = message.replaceAll(String.valueOf(placeholders[index]), String.valueOf(placeholders[index + 1]));
		return Utils.applyColorCodes(message);
	}

	public static String getMessage(String dir, Object... placeholders) {
		if(!config.contains(dir)) {
			GameAPI.GAME_PROVIDER.getLogger().info("Cannot find the message '" + dir + "'.");
			return dir;
		}
		String message = ChatColor.RESET + config.getString(dir);
		for (int index = 0; index <= placeholders.length - 1; index += 2)
			message = message.replaceAll(String.valueOf(placeholders[index]), String.valueOf(placeholders[index + 1]));
		if(message.contains("%prefix%"))
			message = message.replaceAll("%prefix%", GameAPI.ACTIVE_GAME.prefix());
		return Utils.applyColorCodes(message
				.replaceAll("%online%", String.valueOf(Utils.getOnlinePlayers().size()))
				.replaceAll("%maxplayer%", String.valueOf(Bukkit.getMaxPlayers())));
	}

	public static List<String> getMessageList(String dir, Object... placeholders) {
		List<String> result = new ArrayList<>();
		for(String msg : config.getStringList(dir)) {
			for (int index = 0; index <= placeholders.length - 1; index += 2)
				msg = msg.replaceAll(String.valueOf(placeholders[index]), String.valueOf(placeholders[index + 1]));
			if(msg.contains("%prefix%"))
				msg = msg.replaceAll("%prefix%", GameAPI.ACTIVE_GAME.prefix());
			result.add(Utils.applyColorCodes(msg
					.replaceAll("%online%", String.valueOf(Utils.getOnlinePlayers().size()))
					.replaceAll("%maxplayer%", String.valueOf(Bukkit.getMaxPlayers()))));
		}
		if(result.isEmpty())
			return Arrays.asList(dir);
		return result;
	}

	public static void sendMessage(CommandSender p, String dir, Object... placeholders) {
		p.sendMessage(getMessage(dir, placeholders));
	}

	public static void sendMessageList(CommandSender p, String dir, Object... placeholders) {
		getMessageList(dir, placeholders).forEach((s) -> p.sendMessage(Utils.applyColorCodes(s)));
	}
	
	public static void broadcastMessage(String dir, Object... placeholders) {
		Bukkit.broadcastMessage(getMessage(dir, placeholders));
	}
	
	public static void broadcastMessageList(String dir, Object... placeholders) {
		getMessageList(dir, placeholders).forEach((s) -> Bukkit.broadcastMessage(s));
	}

	public static void load(Plugin pl) {
		configFile = new File(pl.getDataFolder().getAbsolutePath() + File.separator + "messages.yml");
		if (!configFile.exists())
			Utils.copy(pl, "messages.yml", configFile);
		config = YamlConfiguration.loadConfiguration(configFile);
	}
}
