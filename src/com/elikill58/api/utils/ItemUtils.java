package com.elikill58.api.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import com.elikill58.api.Messages;
import com.elikill58.api.Version;
import com.elikill58.api.builders.ItemStackBuilder;
import com.elikill58.api.game.GameAPI;

public class ItemUtils {

	/*
	 * Item for compatibility between 1.8 and 1.15
	 * 
	 */
	public static final Material BED = getMaterialWithCompatibility("RED_BED", "BED");

	public static final ItemStack INK_GREEN;
	public static final ItemStack INK_RED;

	static {
		ItemStackBuilder inkGreenBuilder;
		ItemStackBuilder inkRedBuilder;
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			inkGreenBuilder = new ItemStackBuilder(Material.valueOf("LIME_DYE"));
			inkRedBuilder = new ItemStackBuilder(Material.valueOf("RED_DYE"));
		} else {
			inkGreenBuilder = new ItemStackBuilder(Material.valueOf("INK_SACK")).durability((short) 10);
			inkRedBuilder = new ItemStackBuilder(Material.valueOf("INK_SACK")).durability((short) 1);
		}
		INK_GREEN = inkGreenBuilder.displayName(Messages.getMessage("lobby.help.name", "%state%", Messages.getMessage("item-yes"))).build();
		INK_RED = inkRedBuilder.displayName(Messages.getMessage("lobby.help.name", "%state%", Messages.getMessage("item-no"))).build();
	}
	
	public static Material getMaterialWithCompatibility(String... tempMat) {
		for(String s : tempMat) {
			try {
				Material m = (Material) Material.class.getField(s).get(Material.class);
				if(m != null)
					return m;
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e2) {
				e2.printStackTrace();
			} catch (NoSuchFieldException e) {}
		}
		String temp = "";
		for(String s : tempMat)
			temp = temp + (temp.equalsIgnoreCase("") ? "" : ", ") + s;
		GameAPI.GAME_PROVIDER.getLogger().severe("Failed to find Material " + temp);
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static int getDurability(ItemStack item) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			return ((Damageable) item.getItemMeta()).getDamage();
		} else
			return item.getDurability();
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItem(byte color, String defaultName, String endName) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_13))
			return new ItemStack(Material.valueOf(getName(color, defaultName, endName)));
		return new ItemStack(Material.valueOf(getName(color, defaultName, endName)), 1, color);
	}

	public static String getStainedClayName(byte blockColor) {
		return getName(blockColor, "STAINED_CLAY", "TERRACOTTA");
	}
	
	public static String getName(byte color, String defaultName, String endName) {
		if(!Version.getVersion().isNewerOrEquals(Version.V1_13))
			return defaultName;
		if(color == 0)
			return "WHITE_" + endName;
		if(color == 1)
			return "ORANGE_" + endName;
		if(color == 2)
			return "MAGENTA_" + endName;
		if(color == 3)
			return "LIGHT_BLUE_" + endName;
		if(color == 4)
			return "YELLOW_" + endName;
		if(color == 5)
			return "LIME_" + endName;
		if(color == 6)
			return "PINK_" + endName;
		if(color == 7)
			return "GRAY_" + endName;
		if(color == 8)
			return "LIGHT_GRAY_" + endName;
		if(color == 9)
			return "CYAN_" + endName;
		if(color == 10)
			return "PURPLE_" + endName;
		if(color == 11)
			return "BLUE_" + endName;
		if(color == 12)
			return "BROWN_" + endName;
		if(color == 13)
			return "GREEN_" + endName;
		if(color == 14)
			return "RED_" + endName;
		if(color == 15)
			return "BLACK_" + endName;
		return null;
	}
	
	/*public static boolean checkTeam(Team t, ItemStack item) {
		if(item == null)
			return false;
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			return t.getMaterialColored() == item.getType();
		} else {
			return t.getMaterialColored() == item.getType() && t.getBlockColor() == getDurability(item);
		}
	}*/
	
	public static boolean checkTeam(ItemStack t, ItemStack item) {
		if(item == null)
			return false;
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			return t.getType() == item.getType();
		} else {
			return t.getType() == item.getType() && getDurability(t) == getDurability(item);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static boolean checkItem(byte b, Block block) {
		if(block == null)
			return false;
		String name = getStainedClayName(b);
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			return name.equals(block.getType().name());
		} else {
			return name.equals(block.getType().name()) && b == block.getData();
		}
	}
}
