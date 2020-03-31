package com.aotmc.attackontitan.odmgear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.aotmc.attackontitan.AttackOnTitan;

public class ODMData {

	// Map of all active hooks
	private Map<UUID, Hook> hooks = new HashMap<UUID, Hook>();
	
	// Map of all players' hooks
	private Map<UUID, List<Hook>> playerHooks = new HashMap<UUID, List<Hook>>();
	
	// Map of all players' distances for first attached hook
	private Map<UUID, Double> distanceHooks = new HashMap<UUID, Double>();
	
	// Map of all players' tasks for landing
	private Map<UUID, Integer> playerTasks = new HashMap<UUID, Integer>();

	// List of all players who have their first hook attached
	private List<UUID> attachedHook = new ArrayList<UUID>();
	
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
	 * Returns map of all players' distances for first attached hook
	 * 
	 * @return		map of all players' distances for first attached hook
	 */
	public Map<UUID, Double> getDistanceHooks() {
		return distanceHooks;
	}

	/**
	 * Returns map of all players' tasks for landing
	 * 
	 * @return		map of all players' tasks for landing
	 */
	public Map<UUID, Integer> getPlayerTasks() {
		return playerTasks;
	}

	/**
	 * Returns list of all players who have their first hook attached
	 * 
	 * @return		list of all players who have their first hook attached
	 */
	public List<UUID> getAttachedHook() {
		return attachedHook;
	}
	
}
