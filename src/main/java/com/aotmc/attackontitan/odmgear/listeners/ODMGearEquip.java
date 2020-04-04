package com.aotmc.attackontitan.odmgear.listeners;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aotmc.attackontitan.ArmorEquipEvent;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.codeitforyou.lib.api.item.ItemUtil;

public class ODMGearEquip implements Listener {
	
	private ODMData data;
	
	public ODMGearEquip(ODMData data) {
		this.data = data;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEquip(ArmorEquipEvent event) {
		if (event.getOldArmorPiece() != null && Boolean.valueOf(ItemUtil.getNBTString(event.getOldArmorPiece(), "odm"))) {
			if (event.getNewArmorPiece() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
				if (data.getWearingODM() != null && data.getWearingODM().containsKey(event.getPlayer().getUniqueId())) {
					data.getWearingODM().get(event.getPlayer().getUniqueId()).remove();
					data.getWearingODM().remove(event.getPlayer().getUniqueId());
				}
				return;
			}
			return;
		}
		if (event.getNewArmorPiece() == null) {
			return;
		}
        
		Player player = event.getPlayer();
		
		if (!Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
			return;
		}
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(1);
		item.setItemMeta(meta);
		player.getInventory().setHelmet(item);
	}

}
