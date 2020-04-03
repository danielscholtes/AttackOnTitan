package com.aotmc.attackontitan.odmgear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;

public class ODMData {

	// Map of all active hooks
	private Map<UUID, Hook> hooks = new HashMap<UUID, Hook>();
	
	// Map of all players' hooks
	private Map<UUID, List<Hook>> playerHooks = new HashMap<UUID, List<Hook>>();
	
	// Map of all players' distances for first attached hook
	private Map<UUID, Location> locationHooks = new HashMap<UUID, Location>();
	
	// Map of all players' tasks for landing
	private Map<UUID, Integer> playerTasksLanding = new HashMap<UUID, Integer>();
	
	// Map of all players' tasks for potion effect
	private Map<UUID, Integer> playerTasksEffect = new HashMap<UUID, Integer>();

	// List of all players who have their first hook attached
	private List<UUID> attachedHook = new ArrayList<UUID>();
	
	// List of all players currently boosting
	private List<UUID> boosting = new ArrayList<UUID>();
	
	private AttackOnTitan plugin;
	
	public ODMData(AttackOnTitan plugin) {
		this.plugin = plugin;
		startFollowTask();
	}
	
	/**
	 * Creates a task which will make a silverfish follow the player
	 */
	private void startFollowTask() {
		/*
		 * Every 2 ticks makes the silverfish teleport to player
		 */
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				if (hooks != null) {
					for (Hook hook : hooks.values()) {
						if (Bukkit.getPlayer(hook.getPlayer()) == null || hook.getPlayerEntity() == null) {
							continue;
						}
						hook.getPlayerEntity().teleport(Bukkit.getPlayer(hook.getPlayer()).getLocation().add(0, -0.5D, 0D));
					}
				}
			}
		}, 3L, 2L).getTaskId();
	}
	
	public void startBoostTask() {
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				if (boosting != null && !boosting.isEmpty()) {
					Iterator<UUID> iterator = boosting.iterator();
					UUID nextUUID = null;
					while (iterator.hasNext()) {
						nextUUID = iterator.next();
						if (Bukkit.getPlayer(nextUUID) == null) {
							boosting.remove(nextUUID);
						}
						Bukkit.getPlayer(nextUUID).getWorld().spawnParticle(Particle.CLOUD, Bukkit.getPlayer(nextUUID).getLocation(), 10, 0, 0, 0);
					}
				}
			}
		}, 3L, 1L);
	}
	
	public void startPreventFlyTask() {
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getGameMode() != GameMode.SURVIVAL) {
						return;
					}
					player.setFlying(false);
				}
			}
		}, 3L, 7L);
	}
	
	/**
	 * Returns map of all active hooks
	 * 
	 * @return		map of all active hooks
	 */
	public Map<UUID, Hook> getHooks() {
		return hooks;
	}

	/**
	 * Returns map of all players' hooks
	 * 
	 * @return		map of all players' hooks
	 */
	public Map<UUID, List<Hook>> getPlayerHooks() {
		return playerHooks;
	}

	/**
	 * Returns map of all location' distances for first attached hook
	 * 
	 * @return		map of all players' location for first attached hook
	 */
	public Map<UUID, Location> getLocationHooks() {
		return locationHooks;
	}

	/**
	 * Returns map of all players' tasks for landing
	 * 
	 * @return		map of all players' tasks for landing
	 */
	public Map<UUID, Integer> getPlayerTasksLanding() {
		return playerTasksLanding;
	}
	
	/**
	 * Returns map of all players' tasks for effects
	 * 
	 * @return		map of all players' tasks for effects
	 */
	public Map<UUID, Integer> getPlayerTasksEffect() {
		return playerTasksEffect;
	}

	/**
	 * Returns list of all players who have their first hook attached
	 * 
	 * @return		list of all players who have their first hook attached
	 */
	public List<UUID> getAttachedHook() {
		return attachedHook;
	}

	/**
	 * Returns list of all players who are boosting
	 * 
	 * @return		list of all players who are boosting
	 */
	public List<UUID> getBoosting() {
		return boosting;
	}
	
}
