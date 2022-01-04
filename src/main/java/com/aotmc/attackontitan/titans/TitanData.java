package com.aotmc.attackontitan.titans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;
import org.bukkit.entity.Zombie;

public class TitanData {
	
	private AttackOnTitan plugin;
	private Random rand = new Random();
	private List<Titan> toRemove = new ArrayList<>();
	
	public TitanData(AttackOnTitan plugin) {
		this.plugin = plugin;
	}
	
	private Map<Integer, Titan> titans = new HashMap<Integer, Titan>();
	
	private Map<UUID, Integer> grabbedPlayers = new HashMap<UUID, Integer>();
	
	public Map<Integer, Titan> getTitans() {
		return titans;
	}
	
	public Map<UUID, Integer> getGrabbedPlayers() {
		return grabbedPlayers;
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
		Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			if (titans != null) {
				titanloop:
				for (Titan titan : titans.values()) {
					if (!titan.getGiant().isValid() || !titan.getSlime().isValid()) {
						toRemove.add(titan);
						continue titanloop;
					}

					/*
					if (!titan.isGrabbing()) {
						entityloop:
						for (Entity entity : titan.getSlime().getNearbyEntities(5, 7, 5)) {
							if (!(entity instanceof Player)) {
								continue entityloop;
							}
							if (Math.random() < 0.4 && !grabbedPlayers.containsKey(entity.getUniqueId())) {
								titan.grabPlayer((entity).getUniqueId());
							}
							continue titanloop;
						}
					}*/
				}
				if (toRemove != null) {
					for (Titan titan : toRemove) {
						titans.remove(titan.getSlime().getEntityId());
						titan.remove();
					}
					toRemove.clear();
				}
			}
		}, 3L, 20 * 5L).getTaskId();
	}

}
