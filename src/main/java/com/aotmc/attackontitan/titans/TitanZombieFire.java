package com.aotmc.attackontitan.titans;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class TitanZombieFire implements Listener {
	
	@EventHandler
	public void onFire(EntityCombustEvent event) {
		
		if (!(event.getEntity() instanceof Zombie)) {
			return;
		}
		
		event.setCancelled(true);
		
	}

}