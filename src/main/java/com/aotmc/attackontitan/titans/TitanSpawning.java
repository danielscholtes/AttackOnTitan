package com.aotmc.attackontitan.titans;

import com.aotmc.attackontitan.AttackOnTitan;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffectType;

public class TitanSpawning implements Listener {

	private TitanData titanData;

	public TitanSpawning(TitanData titanData) {
		this.titanData = titanData;
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		EntityType type = event.getEntityType();
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		if (type == EntityType.GIANT || type == EntityType.ARMOR_STAND || type == EntityType.BAT || type == EntityType.SQUID ||
				type == EntityType.SILVERFISH || type == EntityType.SLIME || type == EntityType.CHICKEN ||
				(type == EntityType.ZOMBIE && event.getEntity().hasPotionEffect(PotionEffectType.INVISIBILITY))) {
			return;
		}
		if (Math.random() >= 0.05) {
			return;
		}
		event.setCancelled(true);
		boolean cantSpawn = true;
		int spawnY = event.getLocation().getBlockY();
		mainloop:
		while (cantSpawn) {
			for (int i = -5; i <= 5; i++) {
				Block block = event.getLocation().getWorld().getBlockAt(event.getLocation().getBlockX(), spawnY + i, event.getLocation().getBlockZ());
				if (block.getType() != Material.AIR) {
					spawnY++;
					continue mainloop;
				}
				cantSpawn = false;
			}
		}
		new Titan(event.getLocation().clone().add(0, spawnY - (event.getLocation().getY()), 0), TitanType.getRandomSize(), titanData);
	}
	


}
