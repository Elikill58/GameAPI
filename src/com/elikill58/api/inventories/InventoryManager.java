package com.elikill58.api.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryManager implements Listener {

	private static final HashMap<InventoryType, AbstractInventory> INV = new HashMap<>();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		if (item == null || e.getClickedInventory() == null || !(e.getWhoClicked() instanceof Player))
			return;
		Player p = (Player) e.getWhoClicked();
		InventoryHolder holder = e.getClickedInventory().getHolder();
		if(!(holder instanceof GameHolder))
			return;
		GameHolder nh = (GameHolder) holder;
		INV.values().stream().filter((inv) -> inv.isInstance(nh)).findFirst().ifPresent((inv) -> {
			e.setCancelled(true);
			Material m = item.getType();
			if (m.equals(Material.BARRIER))
				p.closeInventory();
			else
				inv.manageInventory(e, p, m, nh);
			return;
		});
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory() == null || !(e.getPlayer() instanceof Player))
			return;
		InventoryHolder holder = e.getInventory().getHolder();
		if(!(holder instanceof GameHolder))
			return;
		for(AbstractInventory inv : INV.values()) {
			if(inv.isInstance((GameHolder) holder)) {
				inv.closeInventory(e, (Player) e.getPlayer());
			}
		}
	}
	
	public static void registerInventory(InventoryType type, AbstractInventory inv) {
		INV.put(type, inv);
	}
	
	public static AbstractInventory get(InventoryType type) {
		return INV.get(type);
	}
}
