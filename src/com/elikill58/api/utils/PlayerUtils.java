package com.elikill58.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static com.elikill58.api.utils.Utils.applyColorCodes;

public class PlayerUtils {


    public static void setPlayerPrefix(Player update, String prefix) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team team = getTeam(p.getScoreboard(), update.getName());
            team.addEntry(update.getName());
            team.setPrefix(applyColorCodes(prefix));
        }
    }
    
    public static void setPlayerSuffix(Player update, String suffix) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team team = getTeam(p.getScoreboard(), update.getName());
            team.addEntry(update.getName());
            team.setSuffix(applyColorCodes(suffix));
        }
    }

    public static void setPlayerPrefixSuffix(Player update, String prefix, String suffix) {
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
