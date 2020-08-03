package com.elikill58.api.utils;

import static com.elikill58.api.utils.Utils.applyColorCodes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerUtils {

	public static void setPlayerPrefix(Player update, String prefix) {
		update.setPlayerListName(prefix + update.getName());
		for (Player p : Bukkit.getOnlinePlayers()) {
			Team team = getTeam(p.getScoreboard(), update.getName());
			team.addEntry(update.getName());
			team.setPrefix(applyColorCodes(prefix));
		}
	}

	public static void setPlayerSuffix(Player update, String suffix) {
		update.setPlayerListName(update.getName() + suffix);
		for (Player p : Bukkit.getOnlinePlayers()) {
			Team team = getTeam(p.getScoreboard(), update.getName());
			team.addEntry(update.getName());
			team.setSuffix(applyColorCodes(suffix));

		}
	}

	public static void setPlayerPrefixSuffix(Player update, String prefix, String suffix) {
		update.setPlayerListName(prefix + update.getName() + suffix);
		for (Player p : Bukkit.getOnlinePlayers()) {
			Team team = getTeam(p.getScoreboard(), update.getName());
			team.addEntry(update.getName());
			team.setPrefix(applyColorCodes(prefix));
			team.setSuffix(applyColorCodes(suffix));
		}
	}

	private static Team getTeam(Scoreboard scoreboard, String name) {
		return scoreboard.getTeam(name) == null ? scoreboard.registerNewTeam(name) : scoreboard.getTeam(name);
	}
}
