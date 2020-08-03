package com.elikill58.api.utils;

import static com.elikill58.api.utils.Utils.VERSION;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.elikill58.api.Version;

public class PacketUtils {

	public static Class<?> CRAFT_PLAYER_CLASS, CHAT_SERIALIZER_CLASS, GAME_PROFILE_CLASS;
	public static Class<?> ENUM_PLAYER_INFO = getEnumPlayerInfoAction();
	public static Object UPDATE_DISPLAY_NAME;
	public static Object SCOREBOARD_ACTION_REMOVE, SCOREBOARD_ACTION_CHANGE, SCOREBOARD_HEALTH_DISPLAY;
	
	static {
		try {
			CRAFT_PLAYER_CLASS = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftPlayer");
			Version ver = Version.getVersion();
			if(ver.equals(Version.V1_7)) {
				CHAT_SERIALIZER_CLASS = Class.forName("net.minecraft.server." + VERSION + ".ChatSerializer");
				// For 1.7, there isn't any Enum, it works only thanks to integers.
				UPDATE_DISPLAY_NAME = 3;
				SCOREBOARD_ACTION_CHANGE = 0;
				SCOREBOARD_ACTION_REMOVE = 1;
				SCOREBOARD_HEALTH_DISPLAY = 0;
			} else {
				CHAT_SERIALIZER_CLASS = Class.forName("net.minecraft.server." + VERSION + ".IChatBaseComponent$ChatSerializer");
				UPDATE_DISPLAY_NAME = ENUM_PLAYER_INFO.getField("UPDATE_DISPLAY_NAME").get(ENUM_PLAYER_INFO);
				Class<?> enumScoreboardAction = null;
				if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
					enumScoreboardAction = Class.forName("net.minecraft.server." + VERSION + ".ScoreboardServer").getDeclaredClasses()[0];
				} else {
					enumScoreboardAction = Class.forName("net.minecraft.server." + VERSION + ".PacketPlayOutScoreboardScore").getDeclaredClasses()[0];
				}
				SCOREBOARD_ACTION_CHANGE = enumScoreboardAction.getDeclaredField("CHANGE").get(enumScoreboardAction);
				SCOREBOARD_ACTION_REMOVE = enumScoreboardAction.getDeclaredField("REMOVE").get(enumScoreboardAction);
				Class<?> enumScoreboardHealthDisplay = Class.forName("net.minecraft.server." + VERSION + ".IScoreboardCriteria").getDeclaredClasses()[0];
				SCOREBOARD_HEALTH_DISPLAY = enumScoreboardHealthDisplay.getDeclaredField("INTEGER").get(enumScoreboardHealthDisplay);
			}

			try {
				GAME_PROFILE_CLASS = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
			} catch (ClassNotFoundException ignored) {
				try {
					GAME_PROFILE_CLASS = Class.forName("com.mojang.authlib.GameProfile");
				} catch (ClassNotFoundException ignoredd) {
					GAME_PROFILE_CLASS = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This Map is to reduce Reflection action which take more resources than just RAM action
	 */
	private static final HashMap<String, Class<?>> ALL_CLASS = new HashMap<>();
	
	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @return clazz
	 */
	public static Class<?> getNmsClass(String name){
		if(ALL_CLASS == null)
			Bukkit.broadcastMessage("Wtf ? ALL_CLASS null ! " + name);
		if(ALL_CLASS.containsKey(name))
			return ALL_CLASS.get(name);
		try {
			Class<?> clazz = Class.forName("net.minecraft.server." + VERSION + "." + name);
			ALL_CLASS.put(name, clazz);
			return clazz;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create a new instance of a packet (without any parameters)
	 * 
	 * @param packetName the name of the packet which is in NMS
	 * @return the created packet
	 */
	public static Object createPacket(String packetName) {
		try {
			return getNmsClass(packetName).getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object createChatBaseComponent(String message) {
		try {
			Method method = CHAT_SERIALIZER_CLASS.getDeclaredMethod("a", String.class);
			method.setAccessible(true);
			return method.invoke(CHAT_SERIALIZER_CLASS, "{\"text\": \"" + message + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the current player ping
	 * 
	 * @param p the player
	 * @return the player ping
	 */
	public static int getPing(Player p) {
		try {
			Object entityPlayer = getEntityPlayer(p);
			return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Send the packet to the specified player
	 * 
	 * @param p which will receive the packet
	 * @param packet the packet to sent
	 */
	public static void sendPacket(Player p, Object packet) {
		try {
			Object playerConnection = getPlayerConnection(p);
			playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getPlayerConnection(Player p) {
		try {
			Object entityPlayer = getEntityPlayer(p);
			return entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getEntityPlayer(Player p) {
		try {
			Object craftPlayer = CRAFT_PLAYER_CLASS.cast(p);
			return craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setField(Object src, String fieldName, Object value) {
		try {
			Field field = src.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(src, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getEnumPlayerInfoAction() {
		try {
			for(Class<?> clazz : Class.forName("net.minecraft.server." + VERSION + ".PacketPlayOutPlayerInfo").getDeclaredClasses())
				if(clazz.getName().contains("EnumPlayerInfoAction"))
					return clazz;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void updatePlayerForEveryone(Player update, String name) {
		name = Utils.applyColorCodes(name);
		if (name.length() > 16)
			name = name.substring(0, 16);
		final String listName = name;
		update.setDisplayName(listName);
		Utils.getOnlinePlayers().stream().forEach((p) -> updatePlayer(p, update, listName));
	}

	public static void updatePlayer(Player receiver, Player toChange, String newListName) {
		try {
			Object ep = getEntityPlayer(toChange);
			setField(ep, "listName", getNmsClass("ChatComponentText").getConstructor(String.class).newInstance(newListName));
			Object packet = getNmsClass("PacketPlayOutPlayerInfo").getConstructor(ENUM_PLAYER_INFO, Iterable.class)
					.newInstance(UPDATE_DISPLAY_NAME, ((Iterable<?>) Arrays.asList(ep)));
			sendPacket(receiver, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
