package com.aotmc.attackontitan.odmgear.listeners;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.ODMType;
import com.codeitforyou.lib.api.item.ItemUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
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

		/*
		 * Gets hook ID
		 */
		for (MetadataValue mdv2 : snowball.getMetadata("HookID")) {
			if (data.getHooks().get(UUID.fromString(mdv2.asString())) == null) {
				return;
			}
			Hook hook = data.getHooks().get(UUID.fromString(mdv2.asString()));
			if (event.getHitEntity() != null) {
				/*
				 * Prevents hook from stopping if it hits itself
				 */
				if (event.getHitEntity() instanceof Player || event.getHitEntity() instanceof Silverfish || event.getHitEntity() instanceof ArmorStand) {
					int collisions = 0;
					for (MetadataValue mdv : snowball.getMetadata("Collisions")) {
						collisions = mdv.asInt();
					}
					collisions++;
					if (collisions >= 20) {
						if (data.getPlayerHooks() != null && data.getPlayerHooks().containsKey(hook.getPlayer())) {
							for (Hook playerHook : data.getPlayerHooks().get(hook.getPlayer())) {
								data.getHooks().remove(playerHook.getHookID());
								if (data.getLocationHookLeft().containsKey(hook.getPlayer())) {
									data.getLocationHookLeft().remove(hook.getPlayer());
								}
								if (data.getLocationHookRight().containsKey(hook.getPlayer())) {
									data.getLocationHookRight().remove(hook.getPlayer());
								}
								Bukkit.getScheduler().runTaskLater(plugin, () -> {
									playerHook.remove();
								}, 3L);
							}
							data.getPlayerHooks().remove(hook.getPlayer());
						}
						return;
					}
					Snowball projectile = (Snowball) event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.SNOWBALL);
					projectile.setBounce(false);
					projectile.setShooter(event.getEntity().getShooter());
					projectile.setPersistent(false);
					projectile.setMetadata("HookID", new FixedMetadataValue(plugin, mdv2.asString()));
					projectile.setMetadata("Collisions", new FixedMetadataValue(plugin, collisions));
					projectile.setVelocity(event.getEntity().getVelocity());

					/*
					 * Sends packet for releashing
					 */
					PacketContainer packetUnleash = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
					packetUnleash.getIntegers().write(0, hook.getPlayerEntity().getEntityId());
					packetUnleash.getIntegers().write(1, -1);
					PacketContainer packetLeash = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
					packetLeash.getIntegers().write(0,  hook.getPlayerEntity().getEntityId());
					packetLeash.getIntegers().write(1, projectile.getEntityId());

					for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
						try {
							plugin.getProtocolManager().sendServerPacket(onlinePlayer, packetUnleash);
							plugin.getProtocolManager().sendServerPacket(onlinePlayer, packetLeash);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
					return;
				}
				/*
				 * Checks if it hit a titan and launches
				 */
				if (event.getHitEntity() instanceof Slime || event.getHitEntity() instanceof Giant) {
					if (Bukkit.getPlayer(hook.getPlayer()) == null) {
						return;
					}

					if (hook.getHookVector() == null) {
						continue;
					}

					if (hook.isLeft() && data.getLocationHookLeft().get(hook.getPlayer()) == null) {
						data.getLocationHookLeft().put(hook.getPlayer(), event.getEntity().getLocation());
					}

					if (!hook.isLeft() && data.getLocationHookRight().get(hook.getPlayer()) == null) {
						data.getLocationHookRight().put(hook.getPlayer(), event.getEntity().getLocation());
					}

					if (data.getLocationHookRight().get(hook.getPlayer()) == null || data.getLocationHookLeft().get(hook.getPlayer()) == null) {
						return;
					}

					Player p = Bukkit.getPlayer(hook.getPlayer());

					/*
					 * Plays sound and particles because it's nice and removes player from attached hooks
					 */

					Vector velocity = event.getHitEntity().getLocation().clone().subtract(Bukkit.getPlayer(hook.getPlayer()).getLocation()).toVector().normalize().multiply(3);

					/*
					 * Adjusts velocity for distance
					 */
					double distance = event.getHitEntity().getLocation().distance(p.getLocation());
					velocity.multiply(1 + (distance/50));
					if (velocity.getY() <= 0) {
						velocity.setY(velocity.getY() + 1.85);
					}
					if (velocity.getY() > 2) {
						velocity.setY(velocity.getY() - 0.55);
					}
					launchPlayer(p, hook, velocity);
					return;
				}
				location = event.getHitEntity().getLocation();
			}


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
			if (hook.isLeft() && data.getLocationHookLeft().get(p.getUniqueId()) == null) {
				data.getLocationHookLeft().put(p.getUniqueId(), location);
			}

			if (!hook.isLeft() && data.getLocationHookRight().get(p.getUniqueId()) == null) {
				data.getLocationHookRight().put(p.getUniqueId(), location);
			}

			if (data.getLocationHookRight().get(p.getUniqueId()) == null || data.getLocationHookLeft().get(p.getUniqueId()) == null) {
				return;
			}

			/*
			 * Gets the middle distance between the two distances
			 */

			Location velocityLocation = location;
			Location velocityLocation2;

			if (hook.isLeft()) {
				velocityLocation2 = data.getLocationHookRight().get(p.getUniqueId());
			} else {
				velocityLocation2 = data.getLocationHookLeft().get(p.getUniqueId());
			}

			/*
			 * Gets the middle location between two hooks
			 */
			double midX = (velocityLocation2.getX() + velocityLocation.getX())/2;
			double midY = (velocityLocation2.getY() + velocityLocation.getY())/2;
			double midZ = (velocityLocation2.getZ() + velocityLocation.getZ())/2;
			velocityLocation = new Location(velocityLocation.getWorld(), midX, midY, midZ);

			Vector velocity = velocityLocation.clone().subtract(Bukkit.getPlayer(hook.getPlayer()).getLocation()).toVector().normalize().multiply(3.8);

			/*
			 * Adjusts velocity for distance
			 */
			double distance = velocityLocation.distance(p.getLocation());
			if (distance < 10) {
				launchPlayer(p, hook, velocity.multiply((distance/10)));
				return;
			}
			launchPlayer(p, hook, velocity.multiply((1 + (distance/400))));
			return;
		}
	}
	
	private void launchPlayer(Player player, Hook hook, Vector velocity) {

		if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
			return;
		}

		int tier = Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getLeggings(), "tier"));
		int gas = Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getLeggings(), "gas"));

		gas += 5;
		gas = Math.min(gas, ODMType.find(tier).get().getGasCapacity());

		player.getEquipment().setLeggings(Utils.createODMHoe(tier, gas));

		if (gas != ODMType.find(tier).get().getGasCapacity()) {
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
		}

		data.getPlayerTasksEffect().put(hook.getPlayer(), Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (Bukkit.getPlayer(hook.getPlayer()) == null || Bukkit.getPlayer(hook.getPlayer()).isOnGround()) {
				return;
			}
			Bukkit.getPlayer(hook.getPlayer()).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 7, 0, false, false, false));
		}, 18L).getTaskId());
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
					if (data.getLocationHookLeft().containsKey(player.getUniqueId())) {
						data.getLocationHookLeft().remove(player.getUniqueId());
					}
					if (data.getLocationHookRight().containsKey(player.getUniqueId())) {
						data.getLocationHookRight().remove(player.getUniqueId());
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
	
}
