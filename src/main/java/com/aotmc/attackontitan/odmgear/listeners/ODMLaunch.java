package com.aotmc.attackontitan.odmgear.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;

public class ODMLaunch implements Listener {
	
	private AttackOnTitan plugin;
	private ODMData data;
	
	public ODMLaunch(AttackOnTitan plugin, ODMData data) {
		this.plugin = plugin;
		this.data = data;
	}


	/**
	 * Prevents the hook from damaging entities
	 */
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		/*
		 * Checks if damager is a hook
		 */
		if (!(event.getDamager() instanceof Snowball) || event.getEntity() instanceof Slime) {
			return;
		}
		if (!(event.getDamager()).hasMetadata("HookID")) {
			return;
		}
		
		event.setCancelled(true);
		
	}


	/**
	 * Handles the hook attaching to a block/entity
	 */
	@EventHandler
	public void onHit(ProjectileHitEvent event) {

		/*
		 * Checks if projectile is a hook
		 */
		if (!(event.getEntity() instanceof Snowball)) {
			return;
		}
		
		Snowball snowball = (Snowball) event.getEntity();
		
		if (!snowball.hasMetadata("HookID")) {
			return;
		}
		
		Location location = null;
		/*
		 * Sets the location to the location of block/entity that the hook was attached to
		 */
		if (event.getHitBlock() != null) {
			location = event.getHitBlock().getLocation();
		}
		if (event.getHitEntity() != null) {
			/*
			 * Cancels hook and resets ODM gear if hook hits itself
			 */
			if (event.getHitEntity() instanceof Player || event.getHitEntity() instanceof Silverfish) {
				for (MetadataValue mdv2 : snowball.getMetadata("HookID")) {
					if (data.getHooks().get(UUID.fromString(mdv2.asString())) == null) {
						return;
					}
					Hook hook = data.getHooks().get(UUID.fromString(mdv2.asString()));
					
					/*
					 * Removes hook from all neccessary maps and lists
					 */
					if (data.getPlayerHooks() != null && data.getPlayerHooks().containsKey(hook.getPlayer())) {
						for (Hook playerHook : data.getPlayerHooks().get(hook.getPlayer())) {
							data.getHooks().remove(playerHook.getHookID());
							Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
								@Override
								public void run() {
									playerHook.remove();
								}
							}, 3L);
						}
						data.getPlayerHooks().remove(hook.getPlayer());
					}
					if (data.getAttachedHook() != null && data.getAttachedHook().contains(hook.getPlayer())) {
						data.getAttachedHook().remove(hook.getPlayer());
					}
					if (data.getLocationHooks() != null && data.getLocationHooks().containsKey(hook.getPlayer())) {
						data.getLocationHooks().remove(hook.getPlayer());
					}
					return;
				}
				return;
			}
			if (event.getHitEntity() instanceof Slime) {
				for (MetadataValue mdv2 : snowball.getMetadata("HookID")) {
					if (data.getHooks().get(UUID.fromString(mdv2.asString())) == null) {
						return;
					}
					Hook hook = data.getHooks().get(UUID.fromString(mdv2.asString()));
					
					if (Bukkit.getPlayer(hook.getPlayer()) == null) {
						return;
					}
					
					if (hook.getHookVector() == null) {
						continue;
					}
					
					Player p = Bukkit.getPlayer(hook.getPlayer());
					
					/*
					 * Plays sound and particles because it's nice and removes player from attached hooks
					 */
					data.getAttachedHook().remove(hook.getPlayer());

					Vector velocity = event.getHitEntity().getLocation().subtract(Bukkit.getPlayer(hook.getPlayer()).getLocation()).toVector().normalize().multiply(2.8);
					velocity.setY(velocity.getY() - 0.25);
					
					launchPlayer(p, hook, velocity);
					return;
				}
			}
			location = event.getHitEntity().getLocation();
		}

		/*
		 * Gets the hook object attached to the projectile
		 */
		for (MetadataValue mdv : snowball.getMetadata("HookID")) {
			if (data.getHooks().get(UUID.fromString(mdv.asString())) == null) {
				return;
			}
			
			Hook hook = data.getHooks().get(UUID.fromString(mdv.asString()));
			
			/*
			 * Checks if player is online
			 */
			if (Bukkit.getPlayer(hook.getPlayer()) == null) {
				return;
			}
			
			Player p = Bukkit.getPlayer(hook.getPlayer());
			
			/*
			 * Checks if this is the first attached hook and if so returns and
			 * saves the distance between location and hook and stores 
			 * the player for attached hooks
			 */
			if (data.getAttachedHook() == null || !data.getAttachedHook().contains(hook.getPlayer())) {
				data.getAttachedHook().add(hook.getPlayer());
				double distance = p.getLocation().distance(location);
				if (distance > 70) {
					distance = 70;
				}
				data.getLocationHooks().put(hook.getPlayer(), event.getEntity().getLocation());
				return;
			}
			
			/*
			 * Gets the middle distance between the two distances
			 */
			
			Location velocityLocation = location;
			
			if (data.getLocationHooks() != null && data.getLocationHooks().containsKey(hook.getPlayer())) {
				Location velocityLocation2 = data.getLocationHooks().get(hook.getPlayer());
				double midX = (velocityLocation2.getX() + velocityLocation.getX())/2;
				double midY = (velocityLocation2.getY() + velocityLocation.getY())/2;
				double midZ = (velocityLocation2.getZ() + velocityLocation.getZ())/2;
				velocityLocation = new Location(velocityLocation.getWorld(), midX, midY, midZ);
				data.getLocationHooks().remove(hook.getPlayer());
			}
			

			Vector velocity = velocityLocation.subtract(Bukkit.getPlayer(hook.getPlayer()).getLocation()).toVector().normalize().multiply(5);
			
			launchPlayer(p, hook, velocity);
			
			return;
		}
		
	}
	
	private void launchPlayer(Player player, Hook hook, Vector velocity) {
		/*
		 * Teleports player 1 block above ground because minecraft is glitchy
		 */
		if (player.isOnGround()) {
			player.teleport(player.getLocation().add(0D, 1D, 0D));
		}
		
		/*
		 * Shoots player with a velocity that seemed about right from my testing
		 * Don't mess with these numbers, they're good enough from the testing
		 * We'll eventually make it so the player can upgrade the speed
		 */
		player.setVelocity(player.getVelocity().add(velocity));
		
		/*
		 * Plays sound and particles because it's nice and removes player from attached hooks
		 */
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 1f);
		player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20);
		data.getAttachedHook().remove(hook.getPlayer());
		
		data.getPlayerTasksEffect().put(hook.getPlayer(), Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getPlayer(hook.getPlayer()) == null || Bukkit.getPlayer(hook.getPlayer()).isOnGround()) {
					return;
				}
				Vector playerVelocity = Bukkit.getPlayer(hook.getPlayer()).getVelocity();
				Bukkit.getPlayer(hook.getPlayer()).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 3, 0, false, false));
				Bukkit.getPlayer(hook.getPlayer()).setVelocity(playerVelocity);
			}
		}, 20L).getTaskId());
		/*
		 * Runs a timer till the player lands, when he does land removes the hook
		 * and removes player and hook from all neccessary lists and maps
		 */
		data.getPlayerTasksLanding().put(hook.getPlayer(), new BukkitRunnable() {
			@Override
			public void run() {
				if (player.isOnGround()) {
					if (data.getPlayerHooks() != null && data.getPlayerHooks().containsKey(hook.getPlayer())) {
						for (Hook playerHook : data.getPlayerHooks().get(hook.getPlayer())) {
							playerHook.remove();
							data.getHooks().remove(playerHook.getHookID());
						}
						data.getPlayerHooks().remove(hook.getPlayer());
					}
					if (data.getPlayerTasksLanding() != null && data.getPlayerTasksLanding().containsKey(hook.getPlayer())) {
						data.getPlayerTasksLanding().remove(hook.getPlayer());
					}
					if (data.getPlayerTasksEffect() != null && data.getPlayerTasksEffect().containsKey(hook.getPlayer())) {
						Bukkit.getScheduler().cancelTask(data.getPlayerTasksEffect().get(hook.getPlayer()));
						data.getPlayerTasksEffect().remove(hook.getPlayer());
					}
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 3L, 1L).getTaskId());
	}
	
	@EventHandler
	public void onSilverFishBlock(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof Silverfish) {
			event.setCancelled(true);
		}
	}
	
	public void startLandingTask() {
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				if (data.getPlayerHooks() != null || data.getPlayerHooks().isEmpty()) {
					return;
				}
				for (UUID uuid : data.getPlayerHooks().keySet()) {
					if (Bukkit.getPlayer(uuid) == null || Bukkit.getPlayer(uuid).isOnGround()) {
						for (Hook hook : data.getPlayerHooks().get(uuid)) {
							hook.remove();
							data.getHooks().remove(hook.getHookID());
						}
						data.getPlayerHooks().remove(uuid);
						if (data.getPlayerTasksEffect() != null && data.getPlayerTasksEffect().containsKey(uuid)) {
							Bukkit.getScheduler().cancelTask(data.getPlayerTasksEffect().get(uuid));
							data.getPlayerTasksEffect().remove(uuid);
						}
					}
				}
			}
		}, 3L, 1L);
	}
	
}
