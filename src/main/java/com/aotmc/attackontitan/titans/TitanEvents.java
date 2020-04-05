package com.aotmc.attackontitan.titans;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.item.ItemUtil;

public class TitanEvents implements Listener {
	
	private TitanData titanData;
	
	public TitanEvents(TitanData titanData) {
		this.titanData = titanData;
	}
	
	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onTitanHit(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Slime)) {
			return;
		}
		
		if (!titanData.getTitans().containsKey(event.getEntity().getEntityId())) {
			return;
		}
		event.setCancelled(true);
		
		if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
			Player player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
			
			if (titanData.getGrabbedPlayers() != null && titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
				event.setCancelled(true);
				return;
			}
	        
			if (!Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getItemInMainHand(), "blade"))) {
				return;
			}
			
			double damage = Double.parseDouble(ItemUtil.getNBTString(player.getInventory().getItemInMainHand(), "damage"));
			if (Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getItemInOffHand(), "blade"))) {
				damage += Double.parseDouble(ItemUtil.getNBTString(player.getInventory().getItemInOffHand(), "damage"));
			}
			
			Slime slime = (Slime) event.getEntity();
			player.getWorld().spawnParticle(Particle.BLOCK_CRACK, slime.getLocation().add(0D, 1.25D, 0D), 100, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
			player.getWorld().playSound(player.getLocation(), "blade", 1, 1);
			if (slime.getHealth() <= damage) {
				Bukkit.broadcastMessage(Utils.color("&7A &2" + titanData.getTitans().get(event.getEntity().getEntityId()).getSize() + "-Meter &7Titan has been slain by &2" + player.getName()));
				titanData.getTitans().get(slime.getEntityId()).remove();
				titanData.getTitans().remove(slime.getEntityId());
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation(), 80);
				player.getWorld().spawnParticle(Particle.BLOCK_CRACK, slime.getLocation().add(0D, 1.25D, 0D), 250, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				player.getWorld().spawnParticle(Particle.BLOCK_CRACK, slime.getLocation().add(0D, -3D, 0D), 60, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation().add(0D, -2D, 0D), 80);
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation().add(0D, -4D, 0D), 80);
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation().add(0D, -6D, 0D), 80);
				player.getWorld().playSound(slime.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.5f, 0.4f);
			} else {
				slime.setHealth(slime.getHealth() - damage);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Giant) && !(event.getEntity() instanceof Zombie)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamageChicken(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Chicken)) {
			return;
		}
		for (int id : titanData.getGrabbedPlayers().values()) {
			if (id != event.getEntity().getEntityId()) {
				continue;
			}

			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if (titanData.getGrabbedPlayers() == null || !titanData.getGrabbedPlayers().containsKey((event.getEntity()).getUniqueId())) {
			return;
		}
		
		event.setCancelled(true);
	}

}
