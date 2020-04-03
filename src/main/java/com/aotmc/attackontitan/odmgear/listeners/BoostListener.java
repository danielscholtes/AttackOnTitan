package com.aotmc.attackontitan.odmgear.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.odmgear.ODMData;

public class BoostListener implements Listener {
	
	private ODMData data;
	
	public BoostListener(ODMData data) {
		this.data = data;
	}
	
	@EventHandler
	public void onToggleFlight(PlayerToggleFlightEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		
		if (event.getPlayer().isOnGround()) {
			return;
		}
		
		event.setCancelled(true);
		event.getPlayer().setFlying(false);
		event.getPlayer().setAllowFlight(false);
		event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(event.getPlayer().getLocation().getDirection().normalize().multiply(5)));
		data.getBoosting().add(event.getPlayer().getUniqueId());
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 11, 8, false, false, false));
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 2f, 1f);
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (data.getBoosting() == null || !data.getBoosting().contains(event.getPlayer().getUniqueId())) {
					return;
				}
				event.getPlayer().setAllowFlight(true);
				data.getBoosting().remove(event.getPlayer().getUniqueId());
			}
		}, 10L);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer().setAllowFlight(true);
	}
	
	public void startEffectTask() {
		
	}

}
