package com.aotmc.attackontitan.odmgear.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.odmgear.equip.ArmorEquipEvent;
import com.codeitforyou.lib.api.item.ItemUtil;

public class ODMGearEquip implements Listener {
	
	private ODMData data;
	
	public ODMGearEquip(ODMData data) {
		this.data = data;
	}
	
	@EventHandler
	public void onEquip(ArmorEquipEvent event) {
		if (event.getOldArmorPiece() != null && Boolean.valueOf(ItemUtil.getNBTString(event.getOldArmorPiece(), "odm"))) {
			event.setUpdateOld(true);
			event.setOldArmorPiece(Utils.createODMLeggings());
			if (event.getNewArmorPiece() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
				if (data.getWearingODM() != null && data.getWearingODM().containsKey(event.getPlayer().getUniqueId())) {
					data.getWearingODM().get(event.getPlayer().getUniqueId()).remove();
					data.getWearingODM().remove(event.getPlayer().getUniqueId());
				}
				if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					event.getPlayer().setAllowFlight(false);
					event.getPlayer().setFlying(false);
				}
				return;
			}
			Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), new Runnable() {
				@Override
				public void run() {	
					event.getPlayer().getEquipment().setLeggings(Utils.createODMHoe());
				}
			}, 2L);
			return;
		}
		if (event.getNewArmorPiece() == null) {
			return;
		}
        
		if (!Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
			return;
		}
		
		Player player = event.getPlayer();
		player.setAllowFlight(true);
		
		ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
		player.addPassenger(armorStand);
		data.getWearingODM().put(player.getUniqueId(), armorStand);
		event.setNewArmorPiece(new ItemStack(Material.AIR));
		event.setUpdateNew(true);
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.getEquipment().setLeggings(Utils.createODMHoe());
			}
		}, 2L);
	}

}
