package com.elikill58.api;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import com.elikill58.api.utils.PacketUtils;
import com.elikill58.api.utils.Utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar {

    private ActionBar() {}

    /**
     * Sent a message in the action bar to the specified player
     * Warn: doesn't work for 1.7 players
     *
     * @param player the player which will receive the message
     * @param message the message that will be sent
     */
    public static void sendTo(Player player, String message) {
    	Version ver = Version.getVersion();
    	if(ver.equals(Version.V1_7))
    		return;
    	try {
        	Object chatComp = PacketUtils.createChatBaseComponent(message);
        	if(ver.isNewerOrEquals(Version.V1_16)) {
        		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        	} else if(ver.isNewerOrEquals(Version.V1_12)) {
				Class<?> chatMessageType = PacketUtils.getNmsClass("ChatMessageType");
				Constructor<?> constructor = PacketUtils.getNmsClass("PacketPlayOutChat").getConstructor(PacketUtils.getNmsClass("IChatBaseComponent"), chatMessageType);
				Object packet = constructor.newInstance(chatComp, chatMessageType.getDeclaredField("GAME_INFO").get(null));
		        PacketUtils.sendPacket(player, packet);
			} else {
				Constructor<?> constructor = PacketUtils.getNmsClass("PacketPlayOutChat").getConstructor(PacketUtils.getNmsClass("IChatBaseComponent"), byte.class);
				Object packet = constructor.newInstance(chatComp, (byte) 2);
		        PacketUtils.sendPacket(player, packet);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Send an action bar message to all player
     *
     * @param message the sent message
     */
	public static void sendToAll(String message) {
        Utils.getOnlinePlayers().forEach(player -> sendTo(player, message));
    }

}

