package com.elikill58.api;

import org.bukkit.entity.Player;

import com.elikill58.api.ClickableText.Action;
import com.elikill58.api.ClickableText.MessageComponent;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClickableText1_13 {

	@SuppressWarnings("deprecation")
	public static void send(Player p, MessageComponent mc) {
		TextComponent text = new TextComponent(mc.text);
		if (mc.a == Action.SHOW_TEXT)
			text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(mc.data).create()));
		else if (mc.a == Action.SUGGEST_COMMAND)
			text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, mc.data));
		else if (mc.a == Action.OPEN_URL)
			text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, mc.data));
		else if (mc.a == Action.RUN_COMMAND)
			text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, mc.data));

		if (mc.a2 == Action.OPEN_URL)
			text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, mc.data2));
		else if (mc.a2 == Action.RUN_COMMAND)
			text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, mc.data2));
		p.spigot().sendMessage(text);
	}
}
