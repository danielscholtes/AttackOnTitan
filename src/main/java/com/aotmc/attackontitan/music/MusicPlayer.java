package com.aotmc.attackontitan.music;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;

public class MusicPlayer {
	
	private Map<UUID, Music> listeningToMusic = new HashMap<UUID, Music>();
	private Map<UUID, Integer> playerMusicTask = new HashMap<UUID, Integer>();
	
	public void startMusicPlayer() {
		Bukkit.getScheduler().runTaskTimer(AttackOnTitan.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					playMusicForPlayer(player, Music.getRandomMusic(), false);
				}
			}
		}, 10L, 20 * 10L);
	}
	
	private void playMusicForPlayer(Player player, Music music, boolean override) {
		if (!override && listeningToMusic != null && listeningToMusic.containsKey(player.getUniqueId())) {
			return;
		}
		
		player.playSound(player.getLocation(), music.getName(), SoundCategory.MUSIC, 300, 1);
		listeningToMusic.put(player.getUniqueId(), music);
		int taskID = Bukkit.getScheduler().runTaskLaterAsynchronously(AttackOnTitan.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (listeningToMusic != null && listeningToMusic.containsKey(player.getUniqueId()) && listeningToMusic.get(player.getUniqueId()) == music) {
					listeningToMusic.remove(player.getUniqueId());
				}
				if (playerMusicTask != null && playerMusicTask.containsKey(player.getUniqueId())) {
					playerMusicTask.remove(player.getUniqueId());
				}
			}
		}, 20L * music.getDuration()).getTaskId();
		
		playerMusicTask.put(player.getUniqueId(), taskID);
	}
	
	public Map<UUID, Music> getListeningToMusic() {
		return listeningToMusic;
	}
	
	public Map<UUID, Integer> getPlayerMusicTask() {
		return playerMusicTask;
	}

}
