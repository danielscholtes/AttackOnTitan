package com.aotmc.attackontitan.titans;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.aotmc.attackontitan.AttackOnTitan;

public class TitanData implements Listener {
	
	private AttackOnTitan plugin;
	private Random rand = new Random();
	
	public TitanData(AttackOnTitan plugin) {
		this.plugin = plugin;
	}
	
	private Map<Integer, Titan> titans = new HashMap<Integer, Titan>();
	
	public Map<Integer, Titan> getTitans() {
		return titans;
	}
	
	/**
	 * Creates a task which will make synchronize all entities of a titan
	 */
	public void startFollowTask() {
		/*
		 * Every 2 ticks makes synchronize the entities of a titan
		 */
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				if (titans != null) {
					for (Titan titan : titans.values()) {
						titan.syncEntities();
					}
				}
			}
		}, 3L, 2L).getTaskId();
	}
	
	/**
	 * Creates a task which will make a silverfish follow the player
	 */
	public void startPlayerDetectionTask() {
		/*
		 * Every 2 ticks makes the silverfish teleport to player
		 */
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				if (titans != null) {
					titanloop:
					for (Titan titan : titans.values()) {
						entityloop:
						for (Entity entity : titan.getSlime().getNearbyEntities(5, 5, 5)) {
							if (!(entity instanceof Player)) {
								continue entityloop;
							}
							
							titan.getZombie().setAI(true);
							titan.getZombie().setTarget((LivingEntity) entity);
							continue titanloop;
						}
						boolean chance = rand.nextBoolean();
						if (chance) {
							titan.getZombie().setAI(false);
						} else {
							titan.getZombie().setAI(true);
						}
						titan.getZombie().setTarget(null);
					}
				}
			}
		}, 3L, 20 * 5L).getTaskId();
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

}
