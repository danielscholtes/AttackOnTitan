package com.aotmc.attackontitan.general;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.aotmc.attackontitan.music.MusicPlayer;

public class WorldChangeListener implements Listener {
	
	private MusicPlayer musicPlayer;
	
	public WorldChangeListener(MusicPlayer musicPlayer) {
		this.musicPlayer = musicPlayer;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if (musicPlayer.getPlayerMusicTask() != null && musicPlayer.getPlayerMusicTask().containsKey(event.getPlayer().getUniqueId())) {
			Bukkit.getScheduler().cancelTask(musicPlayer.getPlayerMusicTask().get(event.getPlayer().getUniqueId()));
			musicPlayer.getPlayerMusicTask().remove(event.getPlayer().getUniqueId());
		}
		if (musicPlayer.getListeningToMusic() != null && musicPlayer.getListeningToMusic().containsKey(event.getPlayer().getUniqueId())) {
			event.getPlayer().stopSound(musicPlayer.getListeningToMusic().get(event.getPlayer().getUniqueId()).getName(), SoundCategory.RECORDS);
			musicPlayer.getListeningToMusic().remove(event.getPlayer().getUniqueId());
		}
	}

}
