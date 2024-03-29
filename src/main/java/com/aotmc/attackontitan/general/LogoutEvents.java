package com.aotmc.attackontitan.general;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.aotmc.attackontitan.music.MusicPlayer;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.titans.TitanData;

public class LogoutEvents implements Listener {
	
	private ODMData odmData;
	private TitanData titanData;
	private MusicPlayer musicPlayer;
	
	public LogoutEvents(ODMData odmData, TitanData titanData, MusicPlayer musicPlayer) {
		this.odmData = odmData;
		this.titanData = titanData;
		this.musicPlayer = musicPlayer;
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
		if (odmData.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : odmData.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				odmData.getHooks().remove(playerHook.getHookID());
			}
			odmData.getPlayerHooks().remove(player.getUniqueId());
		}
		if (odmData.getLocationHookLeft().containsKey(player.getUniqueId())) {
			odmData.getLocationHookLeft().remove(player.getUniqueId());
		}
		if (odmData.getLocationHookRight().containsKey(player.getUniqueId())) {
			odmData.getLocationHookRight().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksLanding().get(player.getUniqueId()));
			odmData.getPlayerTasksLanding().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksEffect().get(player.getUniqueId()));
			odmData.getPlayerTasksEffect().remove(player.getUniqueId());
		}
		if (odmData.getBoosting().contains(player.getUniqueId())) {
			odmData.getBoosting().remove(player.getUniqueId());
		}
		if (titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
			titanData.getGrabbedPlayers().remove(player.getUniqueId());
		}
		if (odmData.getWearingODM().containsKey(player.getUniqueId())) {
			odmData.getWearingODM().get(player.getUniqueId()).remove();
			odmData.getWearingODM().remove(player.getUniqueId());
		}
		if (musicPlayer.getListeningToMusic().containsKey(player.getUniqueId())) {
			musicPlayer.getListeningToMusic().remove(player.getUniqueId());
		}
		if (musicPlayer.getPlayerMusicTask().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(musicPlayer.getPlayerMusicTask().get(player.getUniqueId()));
			musicPlayer.getPlayerMusicTask().remove(player.getUniqueId());
		}
		if (odmData.getLastODMActivate().containsKey(player.getUniqueId())) {
			odmData.getLastODMActivate().remove(player.getUniqueId());
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
		if (odmData.getPlayerHooks() != null && odmData.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : odmData.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				odmData.getHooks().remove(playerHook.getHookID());
			}
			odmData.getPlayerHooks().remove(player.getUniqueId());
		}
		if (odmData.getLocationHookLeft().containsKey(player.getUniqueId())) {
			odmData.getLocationHookLeft().remove(player.getUniqueId());
		}
		if (odmData.getLocationHookRight().containsKey(player.getUniqueId())) {
			odmData.getLocationHookRight().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksLanding() != null && odmData.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksLanding().get(player.getUniqueId()));
			odmData.getPlayerTasksLanding().remove(player.getUniqueId());
		}
		if (odmData.getPlayerTasksEffect() != null && odmData.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(odmData.getPlayerTasksEffect().get(player.getUniqueId()));
			odmData.getPlayerTasksEffect().remove(player.getUniqueId());
		}
		if (odmData.getBoosting() != null && odmData.getBoosting().contains(player.getUniqueId())) {
			odmData.getBoosting().remove(player.getUniqueId());
		}
		if (titanData.getGrabbedPlayers() != null && titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
			titanData.getGrabbedPlayers().remove(player.getUniqueId());
		}
		if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(player.getUniqueId())) {
			ArmorStand as = odmData.getWearingODM().get(player.getUniqueId());
			odmData.getWearingODM().remove(player.getUniqueId());
			as.remove();
		}
	}

}
