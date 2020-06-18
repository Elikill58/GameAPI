package com.elikill58.api;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import com.elikill58.api.utils.PacketUtils;
import com.elikill58.api.utils.Utils;

public class ActionBar {

    private ActionBar() {}

    /**
     * Sent a message in the action bar to the specified player
     *
     * @param player the player which will receive the message
     * @param message the message that will be sent
     */
    public static void sendTo(Player player, String message) {
    	try {
        	Object chatComp = PacketUtils.createChatBaseComponent(message);
			if(Version.getVersion().isNewerOrEquals(Version.V1_12)) {
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

