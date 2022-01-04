package com.aotmc.attackontitan.general;

import com.aotmc.attackontitan.AttackOnTitan;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.aotmc.attackontitan.odmgear.ODMData;

public class ArmorStandEvents implements Listener {
	
	private ODMData data;
	
	public ArmorStandEvents(ODMData data) {
		this.data = data;
	}
	
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
		if (!(event.getDismounted() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getDismounted();
		if (data.getWearingODM() == null || !data.getWearingODM().containsKey(player.getUniqueId())) {
			return;
		}

		event.setCancelled(true);
	}

}
