package com.elikill58.api.inventories;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GameHolder implements InventoryHolder {
	
	private InventoryType type;
	
	public GameHolder(InventoryType type) {
		this.type = type;
	}

	public InventoryType getType() {
		return type;
	}
	
	@Override
    public Inventory getInventory() {
        return null;
    }
}
