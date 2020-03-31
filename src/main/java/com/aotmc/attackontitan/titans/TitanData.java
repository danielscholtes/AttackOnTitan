package com.aotmc.attackontitan.titans;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.aotmc.attackontitan.AttackOnTitan;

public class TitanData {
	
	private AttackOnTitan plugin;
	
	public TitanData(AttackOnTitan plugin) {
		this.plugin = plugin;
	}
	
	private List<BaseTitan> titans = new ArrayList<>();
	
	public List<BaseTitan> getTitans() {
		return titans;
	}
	
	/**
	 * Creates a task which will make a silverfish follow the player
	 */
	public void startFollowTask() {
		/*
		 * Every 2 ticks makes the silverfish teleport to player
		 */
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				if (titans != null) {
					for (BaseTitan titan : titans) {
						titan.syncEntities();
					}
				}
			}
		}, 3L, 2L).getTaskId();
	}

}
