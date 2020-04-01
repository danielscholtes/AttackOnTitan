package com.aotmc.attackontitan.titans;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import com.aotmc.attackontitan.AttackOnTitan;

public class TitanData implements Listener {
	
	private AttackOnTitan plugin;
	
	public TitanData(AttackOnTitan plugin) {
		this.plugin = plugin;
	}
	
	private List<BaseTitan> titans = new ArrayList<>();
	
	public List<BaseTitan> getTitans() {
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
					for (BaseTitan titan : titans) {
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
					for (BaseTitan titan : titans) {
						if (titan instanceof SmallTitan) {
							entityloop:
							for (Entity entity : titan.getSlime().getNearbyEntities(3, 4, 3)) {
								if (!(entity instanceof Player)) {
									continue entityloop;
								}
								
								titan.getZombie().setTarget((LivingEntity) entity);
								continue titanloop;
							}
						}
						if (titan instanceof MediumTitan) {
							entityloop:
							for (Entity entity : titan.getSlime().getNearbyEntities(6, 8, 6)) {
								if (!(entity instanceof Player)) {
									continue entityloop;
								}
								
								titan.getZombie().setTarget((LivingEntity) entity);
								continue titanloop;
							}
						}
						if (titan instanceof LargeTitan) {
							entityloop:
							for (Entity entity : titan.getSlime().getNearbyEntities(9, 17, 9)) {
								if (!(entity instanceof Player)) {
									continue entityloop;
								}
								
								titan.getZombie().setTarget((LivingEntity) entity);
								continue titanloop;
							}
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

}
