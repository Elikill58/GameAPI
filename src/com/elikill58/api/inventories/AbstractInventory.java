package com.elikill58.api.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public abstract class AbstractInventory {
	
	public abstract InventoryType getType();
	public abstract void openInventory(Player p, Object... args);
	public void closeInventory(InventoryCloseEvent e, Player p) {}
	public abstract void manageInventory(InventoryClickEvent e, Player p, Material m, GameHolder nh);
}
