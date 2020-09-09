package com.elikill58.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.elikill58.api.Version;
import com.elikill58.api.game.GameAPI;
import com.google.common.collect.Sets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Utils {

	public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];

	public static List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		try {
			Class<?> mcServer = Class.forName("net.minecraft.server." + VERSION + ".MinecraftServer");
			Object server = mcServer.getMethod("getServer").invoke(mcServer);
			Object craftServer = server.getClass().getField("server").get(server);
			Object getted = craftServer.getClass().getMethod("getOnlinePlayers").invoke(craftServer);
			if (getted instanceof Player[])
				for (Player obj : (Player[]) getted)
					list.add(obj);
			else if (getted instanceof List)
				for (Object obj : (List<?>) getted)
					list.add((Player) obj);
			else
				System.out.println("Unknow getOnlinePlayers");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String applyColorCodes(String message) {
		return message.replaceAll("&r", ChatColor.RESET.toString()).replaceAll("&0", ChatColor.BLACK.toString())
				.replaceAll("&1", ChatColor.DARK_BLUE.toString()).replaceAll("&2", ChatColor.DARK_GREEN.toString())
				.replaceAll("&3", ChatColor.DARK_AQUA.toString()).replaceAll("&4", ChatColor.DARK_RED.toString())
				.replaceAll("&5", ChatColor.DARK_PURPLE.toString()).replaceAll("&6", ChatColor.GOLD.toString())
				.replaceAll("&7", ChatColor.GRAY.toString()).replaceAll("&8", ChatColor.DARK_GRAY.toString())
				.replaceAll("&9", ChatColor.BLUE.toString()).replaceAll("&a", ChatColor.GREEN.toString())
				.replaceAll("&b", ChatColor.AQUA.toString()).replaceAll("&c", ChatColor.RED.toString())
				.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString()).replaceAll("&e", ChatColor.YELLOW.toString())
				.replaceAll("&f", ChatColor.WHITE.toString()).replaceAll("&l", ChatColor.BOLD.toString())
				.replaceAll("&o", ChatColor.ITALIC.toString()).replaceAll("&m", ChatColor.STRIKETHROUGH.toString())
				.replaceAll("&n", ChatColor.UNDERLINE.toString()).replaceAll("&k", ChatColor.MAGIC.toString());
	}

	public static ChatColor getColor(String s) {
		if (s.equals("&r"))
			return ChatColor.RESET;
		if (s.equals("&0"))
			return ChatColor.BLACK;
		if (s.equals("&1"))
			return ChatColor.DARK_BLUE;
		if (s.equals("&2"))
			return ChatColor.DARK_GREEN;
		if (s.equals("&3"))
			return ChatColor.DARK_AQUA;
		if (s.equals("&4"))
			return ChatColor.DARK_RED;
		if (s.equals("&5"))
			return ChatColor.DARK_PURPLE;
		if (s.equals("&6"))
			return ChatColor.GOLD;
		if (s.equals("&7"))
			return ChatColor.GRAY;
		if (s.equals("&8"))
			return ChatColor.DARK_GRAY;
		if (s.equals("&9"))
			return ChatColor.BLUE;
		if (s.equals("&a"))
			return ChatColor.GREEN;
		if (s.equals("&b"))
			return ChatColor.AQUA;
		if (s.equals("&c"))
			return ChatColor.RED;
		if (s.equals("&d"))
			return ChatColor.LIGHT_PURPLE;
		if (s.equals("&e"))
			return ChatColor.YELLOW;
		if (s.equals("&f"))
			return ChatColor.WHITE;
		if (s.equals("&l"))
			return ChatColor.BOLD;
		if (s.equals("&o"))
			return ChatColor.ITALIC;
		if (s.equals("&m"))
			return ChatColor.STRIKETHROUGH;
		if (s.equals("&n"))
			return ChatColor.UNDERLINE;
		if (s.equals("&k"))
			return ChatColor.MAGIC;
		return null;
	}

	public static void tpToOneOfServerList(Player p, List<String> srv) {
		tpToServer(p, srv.get(new Random().nextInt(srv.size())));
	}
	
	public static void tpToServer(Player player, String server) {
		sendMessageToBungee(player, "BungeeCord", "Connect", server);
	}

	public static void sendMessageToBungee(Player p, String channel, String... lines) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for (String s : lines)
			out.writeUTF(s);
		p.sendPluginMessage(GameAPI.GAME_PROVIDER, channel, out.toByteArray());
	}

	public static File copy(Plugin pl, String fileName, File f) {
		try (InputStream in = pl.getResource(fileName); OutputStream out = new FileOutputStream(f)) {
			ByteStreams.copy(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public static String timeToString(long millis) {
		long ms = millis;
		long sec = 0;
		long min = 0;
		long h = 0;
		while (ms > 1000) {
			sec++;
			ms -= 1000;
		}
		while (sec > 60) {
			min++;
			sec -= 60;
		}
		while (min > 60) {
			h++;
			min -= 60;
		}
		return (h > 0 ? h + "h " : "") + (min > 0 ? min + "min " : "") + (sec > 0 ? sec + "sec " : "")
				+ (ms > 0 ? ms + "ms " : "");
	}
	
	@SuppressWarnings("deprecation")
	public static Block getTargetBlock(Player p, int distance) {
		Material[] transparentItem = new Material[] {Material.AIR};
		try {
			if(Version.getVersion().isNewerOrEquals(Version.V1_14)) {
				return (Block) p.getClass().getMethod("getTargetBlockExact", int.class).invoke(p, distance);
			} else {
				try {
					return (Block) p.getClass().getMethod("getTargetBlock", Set.class, int.class).invoke(p, (Set<Material>) Sets.newHashSet(transparentItem), distance);
				} catch (NoSuchMethodException e) {}
				try {
					HashSet<Byte> hashSet = new HashSet<>();
					for(Material m : transparentItem)
						hashSet.add((byte) m.getId());
					return (Block) p.getClass().getMethod("getTargetBlock", HashSet.class, int.class).invoke(p, hashSet, distance);
				} catch (NoSuchMethodException e) {}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}
}
