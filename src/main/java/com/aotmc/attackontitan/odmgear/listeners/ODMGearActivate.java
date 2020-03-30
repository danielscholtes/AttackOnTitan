package com.aotmc.attackontitan.odmgear.listeners;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;

public class ODMGearActivate implements Listener {
	
	private AttackOnTitan plugin;
	private ODMData data;
	
	public ODMGearActivate(AttackOnTitan plugin, ODMData data) {
		this.plugin = plugin;
		this.data = data;
	}
	
	/**
	 * Activates the ODM gear by player swapping hand items with the hotkey
	 */
	@EventHandler
	public void onActive(PlayerSwapHandItemsEvent event) {
		
		Player player = event.getPlayer();
		/*
		 * Checks if player is wearing ODM gear
		 * Gear will have a proper check with NBT soon
		 */
		if (player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType() != Material.CHAINMAIL_LEGGINGS) {
			return;
		}
		
		/*
		 * If player is in the process of shooting hooks, cancel that and remove him from all neccessary
		 * lists and maps
		 */
		if (data.getAttachedHook() != null && data.getAttachedHook().contains(player.getUniqueId())) {
			data.getAttachedHook().remove(player.getUniqueId());
		}

		if (data.getPlayerHooks() != null && data.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : data.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				data.getHooks().remove(playerHook.getHookID());
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						playerHook.remove();
					}
				}, 3L);
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
		
		/*
		 * Activates the ODM Gear
		 */
		player.sendMessage("Using ODM");
		event.setCancelled(true);
		activateGear(player);
		
	}

	/**
	 * Prevents silverfish from taking damage
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() == null) {
			return;
		}
		
		if (!(event.getEntity() instanceof Silverfish)) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	/**
	 * Actives the ODM Gear of the specified player
	 * 
	 * @param		player to activate gear for
	 */
	private void activateGear(Player player) {
		/*
		 * Creates hooks and launches them
		 */
		Hook hookLeft = new Hook(player.getUniqueId(), true, plugin);
		Hook hookRight = new Hook(player.getUniqueId(), false, plugin);
		List<Hook> list = new ArrayList<Hook>();
		list.add(hookLeft);
		list.add(hookRight);
		data.getPlayerHooks().put(player.getUniqueId(), list);
		data.getHooks().put(hookRight.getHookID(), hookRight);
		data.getHooks().put(hookLeft.getHookID(), hookLeft);
		hookRight.launchHook();
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				hookLeft.launchHook();
			}
		}, 2L);
	}

}
