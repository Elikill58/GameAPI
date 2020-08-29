package com.elikill58.api;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.elikill58.api.utils.PacketUtils;
import static com.elikill58.api.utils.PacketUtils.sendPacket;

public class Title {

	public static void sendTitle(Player p, int ticks, String titleMessage, String subMessage) {
		try {
			Class<?> ichat = PacketUtils.getNmsClass("IChatBaseComponent");
			Object chatTitle = ichat.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null,
					"{\"text\": \"" + titleMessage + "\"}");
			Class<?> title = PacketUtils.getNmsClass("PacketPlayOutTitle");
			Constructor<?> titleCons = title.getConstructor(title.getDeclaredClasses()[0], ichat, int.class, int.class,
					int.class);
			Class<?> titleActionClass = title.getDeclaredClasses()[0];
			sendPacket(p, titleCons.newInstance(titleActionClass.getField("TITLE").get(null), chatTitle, 0, ticks, 0));
			if (subMessage.equalsIgnoreCase("")) {
				sendPacket(p, titleCons.newInstance(
						titleActionClass.getField("SUBTITLE").get(null), ichat.getDeclaredClasses()[0]
								.getMethod("a", String.class).invoke(null, "{\"text\": \"" + subMessage + "\"}"),
						5, ticks, 5));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendToAll(int ticks, String titleMessage, String subMessage) {
		for (Player p : Bukkit.getOnlinePlayers())
			sendTitle(p, ticks, titleMessage, subMessage);
	}
}