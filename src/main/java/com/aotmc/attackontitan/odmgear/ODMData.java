package com.aotmc.attackontitan.odmgear;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;

public class ODMData {
	
	// Map of all active hooks
	private Map<UUID, Hook> hooks = new WeakHashMap<UUID, Hook>();
	
	// Map of all players' hooks
	private Map<UUID, Set<Hook>> playerHooks = new HashMap<UUID, Set<Hook>>();
	
	// Map of all players' distances for first attached hook
	private Map<UUID, Location> locationRightHook = new HashMap<UUID, Location>();

	// Map of all players' distances for first attached hook
	private Map<UUID, Location> locationLeftHook = new HashMap<UUID, Location>();
	
	// Map of all players' tasks for landing
	private Map<UUID, Integer> playerTasksLanding = new HashMap<UUID, Integer>();
	
	// Map of all players' tasks for potion effect
	private Map<UUID, Integer> playerTasksEffect = new HashMap<UUID, Integer>();

	// List of all players currently boosting
	private Set<UUID> boosting = new HashSet<UUID>();
	
	private Map<UUID, ArmorStand> wearingODM = new HashMap<UUID, ArmorStand>();
	
	private Map<UUID, Long> lastODMActivate = new HashMap<UUID, Long>();
	
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
					UUID nextUUID;
					while (iterator.hasNext()) {
						nextUUID = iterator.next();
						if (Bukkit.getPlayer(nextUUID) == null) {
							boosting.remove(nextUUID);
						}
						Bukkit.getPlayer(nextUUID).getWorld().spawnParticle(Particle.CLOUD, Bukkit.getPlayer(nextUUID).getLocation(), 10, 0, 0, 0, 0.1);
					}
				}
			}
		}, 3L, 1L);
	}
	
	public void startAlignODMTask() {
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				for (UUID uuid : wearingODM.keySet()) {
					wearingODM.get(uuid).setRotation(Bukkit.getPlayer(uuid).getLocation().getYaw(), wearingODM.get(uuid).getLocation().getPitch());
				}
			}
		}, 3L, 5L);
	}
	
	public void startPreventFlyTask() {
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getGameMode() != GameMode.SURVIVAL) {
						continue;
					}
					player.setFlying(false);
				}
			}
		}, 3L, 7L);
	}
	
	public Map<UUID, ArmorStand> getWearingODM() {
		return wearingODM;
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
	public Map<UUID, Set<Hook>> getPlayerHooks() {
		return playerHooks;
	}

	public Map<UUID, Location> getLocationHookRight() {
		return locationRightHook;
	}

	public Map<UUID, Location> getLocationHookLeft() {
		return locationLeftHook;
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
	 * Returns list of all players who are boosting
	 * 
	 * @return		list of all players who are boosting
	 */
	public Set<UUID> getBoosting() {
		return boosting;
	}
	
	public Map<UUID, Long> getLastODMActivate() {
		return lastODMActivate;
	}

	
}
