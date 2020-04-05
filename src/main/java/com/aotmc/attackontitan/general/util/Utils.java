package com.aotmc.attackontitan.general.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.codeitforyou.lib.api.item.ItemBuilder;

public class Utils {

	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static void message(Player player, String message) {
		player.sendMessage(color(message));
	}
	
	@SuppressWarnings("deprecation")
	public static ArmorStand createODMArmorStand(Location location) {
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(1);
		item.setItemMeta(meta);
		ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		armorStand.setInvulnerable(true);
		armorStand.setVisible(false);
		armorStand.setGravity(false);
		armorStand.setCanPickupItems(false);
		armorStand.getEquipment().setHelmet(item);
		armorStand.setCustomNameVisible(false);
		armorStand.setMarker(true);
		armorStand.setPersistent(false);
		return armorStand;
	}
	
	public static ItemStack createODMLeggings() {
		final ItemBuilder builder = new ItemBuilder(Material.CHAINMAIL_LEGGINGS).withName("&7ODM Gear")
				.withNBTString("odm", String.valueOf(true)).withLore("&7odm lore");

		ItemStack item = builder.getItem();
		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(1);
			item.setItemMeta(meta);
		}
		
		return item;
	}

	public static ItemStack createODMHoe() {
		final ItemBuilder builder = new ItemBuilder(Material.DIAMOND_HOE).withName("&7ODM Gear")
				.withNBTString("odm", String.valueOf(true)).withLore("&7odm lore");

		ItemStack item = builder.getItem();
		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(1);
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
}
