package com.aotmc.attackontitan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.titans.TitanData;

public class LogoutEvents implements Listener {
	
	private ODMData odmData;
	private TitanData titanData;
	
	public LogoutEvents(ODMData odmData, TitanData titanData) {
		this.odmData = odmData;
		this.titanData = titanData;
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
		if (odmData.getAttachedHook() != null && odmData.getAttachedHook().contains(player.getUniqueId())) {
			odmData.getAttachedHook().remove(player.getUniqueId());
		}
		if (odmData.getPlayerHooks() != null && odmData.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : odmData.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				odmData.getHooks().remove(playerHook.getHookID());
			}
			odmData.getPlayerHooks().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksLanding() != null && odmData.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksLanding().get(player.getUniqueId()));
			odmData.getPlayerTasksLanding().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksEffect() != null && odmData.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksEffect().get(player.getUniqueId()));
			odmData.getPlayerTasksEffect().remove(player.getUniqueId());
		}
		if (odmData.getLocationHooks() != null && odmData.getLocationHooks().containsKey(player.getUniqueId())) {
			odmData.getLocationHooks().remove(player.getUniqueId());
		}
		if (odmData.getBoosting() != null && odmData.getBoosting().contains(player.getUniqueId())) {
			odmData.getBoosting().remove(player.getUniqueId());
		}
		if (titanData.getGrabbedPlayers() != null && titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
			titanData.getGrabbedPlayers().remove(player.getUniqueId());
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
		if (odmData.getAttachedHook() != null && odmData.getAttachedHook().contains(player.getUniqueId())) {
			odmData.getAttachedHook().remove(player.getUniqueId());
		}
		if (odmData.getPlayerHooks() != null && odmData.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : odmData.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				odmData.getHooks().remove(playerHook.getHookID());
			}
			odmData.getPlayerHooks().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksLanding() != null && odmData.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksLanding().get(player.getUniqueId()));
			odmData.getPlayerTasksLanding().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksEffect() != null && odmData.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksEffect().get(player.getUniqueId()));
			odmData.getPlayerTasksEffect().remove(player.getUniqueId());
		}
		if (odmData.getLocationHooks() != null && odmData.getLocationHooks().containsKey(player.getUniqueId())) {
			odmData.getLocationHooks().remove(player.getUniqueId());
		}
		if (odmData.getBoosting() != null && odmData.getBoosting().contains(player.getUniqueId())) {
			odmData.getBoosting().remove(player.getUniqueId());
		}
	}

}
