package com.aotmc.attackontitan.odmgear.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
				if (event.getPlayer().getPassengers().get(0) != null && event.getPlayer().getPassengers().get(0) instanceof ArmorStand) {
					event.getPlayer().getPassengers().get(0).remove();
				}
				if (data.getWearingODM() != null && data.getWearingODM().containsKey(event.getPlayer().getUniqueId())) {
					data.getWearingODM().remove(event.getPlayer().getUniqueId());
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
		
		ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
		player.addPassenger(armorStand);
		data.getWearingODM().put(player.getUniqueId(), armorStand);
		
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), new Runnable() {
			@Override
			public void run() {	
				player.getEquipment().setLeggings(Utils.createODMHoe());
			}
		}, 2L);
	}

}
