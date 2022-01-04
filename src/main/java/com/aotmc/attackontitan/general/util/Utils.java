package com.aotmc.attackontitan.general.util;

import com.aotmc.attackontitan.blades.BladeType;
import com.aotmc.attackontitan.odmgear.ODMType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.codeitforyou.lib.api.item.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {

	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static void message(Player player, String message) {
		player.sendMessage(color(message));
	}

	public static ItemStack createMenuItem(Material material, String name) {
		return new ItemBuilder(material).withName(name).withNBTString("MenuItem", String.valueOf(true)).getItem();
	}

	public static ItemStack createMenuItem(Material material, String name, List<String> lore) {
		return new ItemBuilder(material).withName(name).withLore(lore).withNBTString("MenuItem", String.valueOf(true)).getItem();
	}

	public static void createMenuBox(Inventory inventory, Material material, String name, int slot) {
		//Row 1
		inventory.setItem(slot, createMenuItem(material, name));
		inventory.setItem(slot + 1, createMenuItem(material, name));
		inventory.setItem(slot + 2, createMenuItem(material, name));

		//Row 2
		inventory.setItem(9 + slot, createMenuItem(material, name));
		inventory.setItem(9 + slot + 2, createMenuItem(material, name));

		//Row 3
		inventory.setItem(18 + slot, createMenuItem(material, name));
		inventory.setItem(18 + slot + 1, createMenuItem(material, name));
		inventory.setItem(18 + slot + 2, createMenuItem(material, name));
	}

	public static void createMenuBox(Inventory inventory, Material material, String name, List<String> lore, int slot) {
		//Row 1
		inventory.setItem(slot, createMenuItem(material, name, lore));
		inventory.setItem(slot + 1, createMenuItem(material, name, lore));
		inventory.setItem(slot + 2, createMenuItem(material, name, lore));

		//Row 2
		inventory.setItem(9 + slot, createMenuItem(material, name, lore));
		inventory.setItem(9 + slot + 2, createMenuItem(material, name, lore));

		//Row 3
		inventory.setItem(18 + slot, createMenuItem(material, name, lore));
		inventory.setItem(18 + slot + 1, createMenuItem(material, name, lore));
		inventory.setItem(18 + slot + 2, createMenuItem(material, name, lore));
	}

	public static ItemStack createNewBlade(final BladeType bladeType) {
		ItemStack item = new ItemBuilder(Material.WOODEN_SWORD)
				.withName(bladeType.getDisplayName())
				.withNBTString("blade", "true")
				.withNBTString("tier", String.valueOf(bladeType.getHierarchy()))
				.withLore(bladeType.getLore())
				.withFlag(ItemFlag.HIDE_ATTRIBUTES).getItem();


		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(1);
			meta.setUnbreakable(true);

			item.setItemMeta(meta);
		}

		return item;
	}

	public static ItemStack createNewGasCanister(final int gas) {
		ItemStack item = new ItemBuilder(Material.GLASS_BOTTLE)
				.withName("&7Gas Canister")
				.withNBTString("gascanister", "true")
				.withNBTString("gas", String.valueOf(gas))
				.withLore(Arrays.asList("", " &fGas&8: &7" + gas, "", " &fRight click to fill up your ODM gear with gas&r  ", ""))
				.withFlag(ItemFlag.HIDE_ATTRIBUTES).getItem();


		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(1);

			item.setItemMeta(meta);
		}

		return item;
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

	public static ItemStack createODMLeggings(int tier, int gas) {
		ODMType odmType = ODMType.find(tier).get();
		List<String> lore = new ArrayList<>();
		lore.addAll(odmType.getLore());
		int cap = odmType.getGasCapacity();
		int used = odmType.getGasCapacity() - gas;
		int percentage = (used * 100)/cap;
		lore.add(" &fGas&8: &7" + used + "&8/&7" + cap + " &8(&7" + percentage + "%&8)");
		lore.add("");
		final ItemBuilder builder = new ItemBuilder(Material.CHAINMAIL_LEGGINGS).withName(odmType.getDisplayName())
				.withNBTString("tier", String.valueOf(tier))
				.withNBTString("gas", String.valueOf(gas))
				.withNBTString("odm", String.valueOf(true))
				.withLore(lore)
				.withFlag(ItemFlag.HIDE_ATTRIBUTES);


		ItemStack item = builder.getItem();
		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(1);
			item.setItemMeta(meta);
		}
		
		return item;
	}

	public static ItemStack createODMHoe(int tier, int gas) {
		ODMType odmType = ODMType.find(tier).get();
		List<String> lore = new ArrayList<>();
		lore.addAll(odmType.getLore());
		int cap = odmType.getGasCapacity();
		int used = odmType.getGasCapacity() - gas;
		int percentage = (used * 100)/cap;
		lore.add(" &fGas&8: &7" + used + "&8/&7" + cap + " &8(&7" + percentage + "%&8)");
		lore.add("");
		final ItemBuilder builder = new ItemBuilder(Material.DIAMOND_HOE).withName(odmType.getDisplayName())
				.withNBTString("tier", String.valueOf(tier))
				.withNBTString("gas", String.valueOf(gas))
				.withNBTString("odm", String.valueOf(true))
				.withLore(lore)
				.withFlag(ItemFlag.HIDE_ATTRIBUTES);

		ItemStack item = builder.getItem();
		ItemMeta meta = item.getItemMeta();

		if (meta != null) {
			meta.setCustomModelData(1);
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
}
