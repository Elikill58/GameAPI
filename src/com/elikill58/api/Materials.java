package com.elikill58.api;

import org.bukkit.Material;

import static com.elikill58.api.utils.ItemUtils.getMaterialWithCompatibility;

public class Materials {

	public static final Material SKULL_ITEM = getMaterialWithCompatibility("PLAYER_HEAD", "SKULL_ITEM");
	public static final Material ENCHANT_TABLE = getMaterialWithCompatibility("ENCHANTMENT_TABLE", "ENCHANTING_TABLE");
	public static final Material BED = getMaterialWithCompatibility("RED_BED", "BED");
	
	
}
