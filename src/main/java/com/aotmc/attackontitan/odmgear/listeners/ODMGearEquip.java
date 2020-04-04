package com.aotmc.attackontitan.odmgear.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.aotmc.attackontitan.ArmorEquipEvent;
import com.codeitforyou.lib.api.item.ItemUtil;

public class ODMGearEquip implements Listener {
	
	@EventHandler
	public void onEquip(ArmorEquipEvent event) {
		if (event.getNewArmorPiece() == null) {
			return;
		}
        
		Player player = event.getPlayer();
		
		if (!Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
			return;
		}
		
	}

}
