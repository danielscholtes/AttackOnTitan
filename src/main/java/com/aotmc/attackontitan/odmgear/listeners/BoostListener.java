package com.aotmc.attackontitan.odmgear.listeners;

import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.ODMType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.util.Vector;

public class BoostListener implements Listener {
	
	private ODMData data;
	
	public BoostListener(ODMData data) {
		this.data = data;
	}
	
	@EventHandler
	public void onToggleFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();

		if (data.getBoostCooldown().contains(player.getUniqueId())) {
			return;
		}

		if (player.getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		
		if (player.isOnGround()) {
			return;
		}
		
		if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
			return;
		}

		event.setCancelled(true);
		player.setFlying(false);
		player.setAllowFlight(false);

		data.getBoostCooldown().add(player.getUniqueId());
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			data.getBoostCooldown().remove(player.getUniqueId());
			player.setAllowFlight(true);
		}, 13L);

		int tier = Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getLeggings(), "tier"));
		int gas = Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getLeggings(), "gas"));

		gas += 10;
		gas = Math.min(gas, ODMType.find(tier).get().getGasCapacity());

		player.getEquipment().setLeggings(Utils.createODMHoe(tier, gas));

		if (gas == ODMType.find(tier).get().getGasCapacity()) {
			return;
		}

		double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
		double pitch = (80 * Math.PI) / 180;

		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		Vector boost = new Vector(x, z, y).normalize();

		player.setVelocity(boost.multiply(2.2));
		//player.setVelocity(player.getVelocity().add(player.getLocation().getDirection().normalize().multiply(2.25)));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8, 8, false, false, false));
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 2f, 1f);

		data.getBoosting().add(player.getUniqueId());
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			if (data.getBoosting() == null || !data.getBoosting().contains(player.getUniqueId())) {
				return;
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 6, 2, false, false, false));
			data.getBoosting().remove(player.getUniqueId());
		}, 8L);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		if (event.getPlayer().getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getPlayer().getInventory().getLeggings(), "odm"))) {
			event.getPlayer().setAllowFlight(false);
			event.getPlayer().setFlying(false);
			return;
		}
		event.getPlayer().setAllowFlight(true);
	}
	
}
