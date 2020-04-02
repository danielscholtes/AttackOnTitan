package com.aotmc.attackontitan.titans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;

public class TitanData {
	
	private AttackOnTitan plugin;
	private Random rand = new Random();
	private List<Titan> toRemove = new ArrayList<Titan>();
	
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
						if (!titan.getGiant().isValid()) {
							toRemove.add(titan);
							continue titanloop;
						}
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
					if (toRemove != null) {
						for (Titan titan : toRemove) {
							titans.remove(titan.getSlime().getEntityId());
							titan.remove();
						}
						toRemove.clear();
					}
				}
			}
		}, 3L, 20 * 5L).getTaskId();
	}

}
