package com.aotmc.attackontitan.titans;

import org.bukkit.Bukkit;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;

public class TitanEvents implements Listener {
	
	private TitanData titanData;
	
	public TitanEvents(TitanData titanData) {
		this.titanData = titanData;
	}
	
	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onTitanHit(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Slime)) {
			return;
		}
		event.setCancelled(true);
		if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
			Bukkit.broadcastMessage("a");
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Giant) && !(event.getEntity() instanceof Zombie)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDespawn(EntityRemoveFromWorldEvent event) {
		if (!event.getEntity().getLocation().getWorld().getName().equalsIgnoreCase("Shiganshina_world")) {
			return;
		}
		
		if (!(event.getEntity() instanceof Slime)) {
			return;
		}
		
		if (titanData.getTitans() == null || !titanData.getTitans().containsKey(event.getEntity().getEntityId())) {
			return;
		}
		
		titanData.getTitans().get(event.getEntity().getEntityId()).remove();
		titanData.getTitans().remove(event.getEntity().getEntityId());
	}

}
