package com.elikill58.api.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryManager implements Listener {

	private static final List<AbstractInventory> INV = new ArrayList<>();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		if (item == null || e.getClickedInventory() == null || !(e.getWhoClicked() instanceof Player))
			return;
		Player p = (Player) e.getWhoClicked();
		InventoryHolder holder = e.getClickedInventory().getHolder();
		if (!(holder instanceof GameHolder))
			return;
		GameHolder gh = (GameHolder) holder;
		AbstractInventory inv = get(gh);
		e.setCancelled(true);
		Material m = item.getType();
		if (m.equals(Material.BARRIER))
			p.closeInventory();
		else
			inv.manageInventory(e, p, m, gh);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory() == null || !(e.getPlayer() instanceof Player))
			return;
		InventoryHolder holder = e.getInventory().getHolder();
		if (!(holder instanceof GameHolder))
			return;
		get((GameHolder) holder).closeInventory(e, (Player) e.getPlayer());
	}

	public static void registerInventory(AbstractInventory inv) {
		INV.add(inv);
	}

	public static AbstractInventory get(GameHolder gh) {
		return INV.stream().filter((inv) -> inv.getType().equals(gh.getType())).findFirst().orElse(null);
	}

	public static AbstractInventory get(InventoryType type) {
		return INV.stream().filter((inv) -> inv.getType().equals(type)).findFirst().orElse(null);
	}
}
