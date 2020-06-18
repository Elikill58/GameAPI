package com.elikill58.api.builders;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.elikill58.api.Version;
import com.elikill58.api.utils.Utils;

public class ItemStackBuilder {

    protected ItemStack itemStack;
    protected ItemMeta itemMeta;

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.hasItemMeta() ? this.itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.itemStack.getType());
    }
    
    public ItemStackBuilder(Material material) { this(new ItemStack(material)); }

    public ItemStackBuilder(Material material, int amount) { this(new ItemStack(material, amount)); }

    public ItemStackBuilder displayName(@Nullable String displayName) {
        this.itemMeta.setDisplayName(ChatColor.RESET + Utils.applyColorCodes(displayName));
        return this;
    }

    public ItemStackBuilder resetDisplayName() {
        return this.displayName(null);
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStackBuilder unsafeEnchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStackBuilder type(Material type) {
        this.itemStack.setType(type);
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    
	@SuppressWarnings("deprecation")
	public ItemStackBuilder durability(short durability) {
		itemStack.setDurability(durability);
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			ItemMeta meta = itemStack.getItemMeta();
			((Damageable) meta).setDamage(durability);
			itemStack.setItemMeta(meta);
		}
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
    	return lore(lore.toArray(new String[] {}));
    }

    public ItemStackBuilder lore(String... lore) {
        List<String> list = this.itemMeta.hasLore() ? this.itemMeta.getLore() : new ArrayList<>();
    	for(String s : lore)
    		for(String temp : s.split("\\n"))
        		for(String tt : temp.split("/n"))
        			list.add(Utils.applyColorCodes(tt));
        this.itemMeta.setLore(list);
        return this;
    }

    public ItemStackBuilder addToLore(String... loreToAdd) {
        return lore(loreToAdd);
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);

        return this.itemStack;
    }
}
