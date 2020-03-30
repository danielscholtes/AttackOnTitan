package com.aotmc.attackontitan.odmgear.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;

public class ODMLogout implements Listener {
	
	private ODMData data;
	
	public ODMLogout(ODMData data) {
		this.data = data;
	}

	/**
	 * Handles hook when players logs out
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		/*
		 * Removes player from all neccessary lists and maps
		 */
		if (data.getAttachedHook() != null && data.getAttachedHook().contains(player.getUniqueId())) {
			data.getAttachedHook().remove(player.getUniqueId());
		}
		if (data.getPlayerHooks() != null && data.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : data.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				data.getHooks().remove(playerHook.getHookID());
			}
			data.getPlayerHooks().remove(player.getUniqueId());
		}
		if (data.getPlayerTasks() != null && data.getPlayerTasks().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(data.getPlayerTasks().get(player.getUniqueId()));
			data.getPlayerTasks().remove(player.getUniqueId());
		}
		if (data.getDistanceHooks() != null && data.getDistanceHooks().containsKey(player.getUniqueId())) {
			data.getDistanceHooks().remove(player.getUniqueId());
		}
	}


	/**
	 * Handles hook when player gets kicked
	 */
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();

		/*
		 * Removes player from all neccessary lists and maps
		 */
		if (data.getAttachedHook() != null && data.getAttachedHook().contains(player.getUniqueId())) {
			data.getAttachedHook().remove(player.getUniqueId());
		}
		if (data.getPlayerHooks() != null && data.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : data.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				data.getHooks().remove(playerHook.getHookID());
			}
			data.getPlayerHooks().remove(player.getUniqueId());
		}
		if (data.getPlayerTasks() != null && data.getPlayerTasks().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(data.getPlayerTasks().get(player.getUniqueId()));
			data.getPlayerTasks().remove(player.getUniqueId());
		}
		if (data.getDistanceHooks() != null && data.getDistanceHooks().containsKey(player.getUniqueId())) {
			data.getDistanceHooks().remove(player.getUniqueId());
		}
	}

}
