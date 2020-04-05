package com.aotmc.attackontitan.general;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class ArmorStandEvents implements Listener {
	
	@EventHandler
	public void onModify(PlayerInteractAtEntityEvent event) {
		if (!(event.getRightClicked() instanceof ArmorStand)) {
			return;
		}
		
		if (((ArmorStand) event.getRightClicked()).isVisible()) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof ArmorStand)) {
			return;
		}
		
		if (((ArmorStand) event.getEntity()).isVisible()) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		if (!(event.getEntity() instanceof ArmorStand)) {
			return;
		}
		
		event.setCancelled(true);
	}

}
