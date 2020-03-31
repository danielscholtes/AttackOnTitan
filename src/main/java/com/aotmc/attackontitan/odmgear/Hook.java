package com.aotmc.attackontitan.odmgear;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.aotmc.attackontitan.AttackOnTitan;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class Hook {
	
	private Projectile projectile;
	private UUID player;
	private UUID hookID;
	private boolean left;
	private AttackOnTitan plugin;
	private Vector vector;
	private Silverfish projectileEntity;
	private Silverfish playerEntity;
	private int taskID;
	
	public Hook(UUID player, boolean left, AttackOnTitan plugin) {
		this.player = player;
		this.left = left;
		this.hookID = UUID.randomUUID();
		this.plugin = plugin;
		this.vector = createVector();
	}

	/**
	 * Launches the hook
	 */
	@SuppressWarnings("deprecation")
	public void launchHook() {
		/*
		 * Checks if player is online
		 */
		if (Bukkit.getPlayer(player) == null) {
			return;
		}
		
		Player p = Bukkit.getPlayer(player);
		
		/*
		 * Creates the silverfish that will be riding the hook
		 */
		projectileEntity = (Silverfish) Bukkit.getPlayer(player).getWorld().spawnEntity(Bukkit.getPlayer(player).getLocation(), EntityType.SILVERFISH);
		projectileEntity.setGravity(false);
		projectileEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
		projectileEntity.setSilent(true);
		projectileEntity.setPersistent(false);
		
		/*
		 * Creates the silverfish that will be following the player
		 */
		playerEntity = (Silverfish) Bukkit.getPlayer(player).getWorld().spawnEntity(Bukkit.getPlayer(player).getLocation(), EntityType.SILVERFISH);
		playerEntity.setGravity(false);
		playerEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0, false, false));
		playerEntity.setSilent(true);
		playerEntity.setPersistent(false);
		
		/*
		 * Creates the hook
		 */
		projectile = p.launchProjectile(Snowball.class);
		projectile.setBounce(false);
		projectile.setShooter(p);
		projectile.setMetadata("HookID", new FixedMetadataValue(plugin, hookID.toString()));
		projectile.addPassenger(projectileEntity);
		projectile.setPersistent(false);
		
		/*
		 * Calculates vector and shoots the hook
		 */
		double yaw = ((p.getLocation().getYaw() + 90)  * Math.PI) / 180;
		double pitch = ((p.getLocation().getPitch() + 88) * Math.PI) / 180;
		if (left) {
			yaw = ((p.getLocation().getYaw() + 95)  * Math.PI) / 180;
		} else {
			yaw = ((p.getLocation().getYaw() + 85)  * Math.PI) / 180;
		}
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		projectile.setVelocity(new Vector(x, z, y).normalize().multiply(5));
		startFollowTask();
		
		/*
		 * Sends a packet to all players to show a leash on the 2 silverfishes so
		 * that it looks like a sort of rope for the hook. Using packets because 
		 * max distance for leash is 10 blocks
		 */
		PacketContainer packetLeash = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
		packetLeash.getIntegers().write(0, playerEntity.getEntityId());
		packetLeash.getIntegers().write(1, projectileEntity.getEntityId());
		
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			try {
				plugin.getProtocolManager().sendServerPacket(onlinePlayer, packetLeash);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a task which will make a silverfish follow the player
	 */
	private void startFollowTask() {
		if (playerEntity == null || Bukkit.getPlayer(player) == null) {
			return;
		}
		
		/*
		 * Every 2 ticks makes the silverfish teleport to player
		 */
		taskID = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
			@Override
			public void run() {
				playerEntity.teleport(Bukkit.getPlayer(player).getLocation().add(0, -0.5D, 0D));
			}
		}, 3L, 2L).getTaskId();
	}

	/**
	 * Removes all the things related to this hook object
	 */
	public void remove() {
		/*
		 * Stops following task and removes leash packet and removes entities
		 */
		Bukkit.getScheduler().cancelTask(taskID);
		if (playerEntity != null) {
			PacketContainer packetLeash = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
			packetLeash.getIntegers().write(0, playerEntity.getEntityId());
			packetLeash.getIntegers().write(1, -1);
			
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				try {
					plugin.getProtocolManager().sendServerPacket(onlinePlayer, packetLeash);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			playerEntity.remove();
		}
		if (projectileEntity != null) {
			projectileEntity.remove();
		}
		if (projectile != null) {
			projectile.remove();
		}
	}


	/**
	 * Calculates and sets the vector for player to be shot at
	 */
	private Vector createVector() {
		if (Bukkit.getPlayer(player) == null) {
			return null;
		}
		
		Player p = Bukkit.getPlayer(player);
		
		double yaw = ((p.getEyeLocation().getYaw() + 90)  * Math.PI) / 180;
		double pitch = ((p.getEyeLocation().getPitch() + 77) * Math.PI) / 180;
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		
		return new Vector(x, z, y).normalize();
	}
	
	/**
	 * Returns the player UUID set for this hook
	 *
	 * @return		player UUID
	 */
	public UUID getPlayer() {
		return player;
	}

	/**
	 * Returns the hook UUID set for this hook
	 *
	 * @return		hook UUID
	 */
	public UUID getHookID() {
		return hookID;
	}

	/**
	 * Returns the vector that was calculated for the player
	 * The player will be shot with this velocity
	 *
	 * @return		vector for player
	 */
	public Vector getVector() {
		return vector;
	}

	/**
	 * Returns the projectile created for this hook
	 *
	 * @return		projectile of hook
	 */
	public Projectile getProjectile() {
		return projectile;
	}

	/**
	 * Returns whether or not the boolean is going left or right
	 *
	 * @return		boolean
	 */
	public boolean isLeft() {
		return left;
	}

}
