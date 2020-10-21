package com.elikill58.api;

import static com.elikill58.api.utils.ItemUtils.getMaterialWithCompatibility;

import org.bukkit.Material;

public class Materials {

	public static final Material SKULL_ITEM = getMaterialWithCompatibility("PLAYER_HEAD", "SKULL_ITEM");
	public static final Material ENCHANT_TABLE = getMaterialWithCompatibility("ENCHANTMENT_TABLE", "ENCHANTING_TABLE");
	public static final Material BED = getMaterialWithCompatibility("RED_BED", "BED");
	public static final Material WORKBENCH = getMaterialWithCompatibility("CRAFTING_TABLE", "WORKBENCH");
	public static final Material SUGAR_CANE = getMaterialWithCompatibility("SUGAR_CANE_BLOCK", "SUGAR_CANE");
	
	public static final Material DIAMOND_SPADE = getMaterialWithCompatibility("DIAMOND_SPADE", "DIAMOND_SHOVEL");
	public static final Material DIAMOND_AXE = getMaterialWithCompatibility("DIAMOND_AXE");
	public static final Material DIAMOND_PICKAXE = getMaterialWithCompatibility("DIAMOND_PICKAXE");
	public static final Material DIAMOND_SWORD = getMaterialWithCompatibility("DIAMOND_SWORD");
	
	public static final Material IRON_SPADE = getMaterialWithCompatibility("IRON_SPADE", "IRON_SHOVEL");
	public static final Material IRON_AXE = getMaterialWithCompatibility("IRON_AXE");
	public static final Material IRON_PICKAXE = getMaterialWithCompatibility("IRON_PICKAXE");
	public static final Material IRON_SWORD = getMaterialWithCompatibility("IRON_SWORD");
	
	public static final Material GOLD_SPADE = getMaterialWithCompatibility("GOLD_SPADE", "GOLD_SHOVEL", "GOLDEN_SHOVEL");
	public static final Material GOLD_AXE = getMaterialWithCompatibility("GOLD_AXE", "GOLDEN_AXE");
	public static final Material GOLD_PICKAXE = getMaterialWithCompatibility("GOLD_PICKAXE", "GOLDEN_PICKAXE");
	public static final Material GOLD_SWORD = getMaterialWithCompatibility("GOLD_SWORD", "GOLDEN_SWORD");
	
	public static final Material WOOD_SPADE = getMaterialWithCompatibility("WOOD_SPADE", "WOODEN_SHOVEL");
	public static final Material WOOD_AXE = getMaterialWithCompatibility("WOOD_AXE", "WOODEN_AXE");
	public static final Material WOOD_PICKAXE = getMaterialWithCompatibility("WOOD_PICKAXE", "WOODEN_PICKAXE");
	public static final Material WOOD_SWORD = getMaterialWithCompatibility("WOOD_SWORD", "WOODEN_SWORD");
	
}
