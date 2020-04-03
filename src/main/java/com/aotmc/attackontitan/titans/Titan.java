package com.aotmc.attackontitan.titans;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.aotmc.attackontitan.AttackOnTitan;

import net.md_5.bungee.api.ChatColor;

public class Titan {
	
	private Giant giant;
	private Zombie zombie;
	private Slime slime;
	private int size;
	private TitanType titanType;
	private int grabTaskID = -1;
	private UUID grabbedPlayer;

	public Titan(Location spawnLocation, TitanType titanType, int size) {
		this.size = size;
		this.titanType = titanType;
		spawnTitan(spawnLocation);
	}


	@SuppressWarnings("deprecation")
	public void spawnTitan(Location spawnLocation) {
		zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999999, 1, false, false));
		zombie.setSilent(true);
		zombie.setCustomNameVisible(false);
		zombie.setCanPickupItems(false);
		zombie.setInvulnerable(true);
		zombie.setTarget(null);
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		zombie.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
		if (zombie.getVehicle() != null) {
			zombie.getVehicle().remove();
		}
		
		giant = (Giant) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.GIANT);
		giant.setSilent(true);
		giant.setCustomNameVisible(true);
		giant.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6" + size + " Meter Titan"));
		giant.setCanPickupItems(false);
		giant.setInvulnerable(true);
		giant.setAI(false);
		
		slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 8D, 0D), EntityType.SLIME);
		slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		slime.setSize(5);
		slime.setCustomNameVisible(false);
		slime.setCanPickupItems(false);
		slime.setAI(false);
		slime.setGravity(false);
		slime.setMaxHealth(20);
		slime.setHealth(slime.getMaxHealth());
	}
	 
	public int getSize() {
		return size;
	}
	 
	public TitanType getType() {
		return titanType;
	}
	
	public Giant getGiant() {
		return this.giant;
	}
	
	@SuppressWarnings("deprecation")
	public void createNewGiant() {
		this.giant.remove();
		this.giant = (Giant) this.slime.getLocation().getWorld().spawnEntity(this.slime.getLocation().add(0D, -8D, 0D), EntityType.GIANT);
		this.giant.setSilent(true);
		this.giant.setCustomNameVisible(true);
		this.giant.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6" + size + " Meter Titan"));
		this.giant.setCanPickupItems(false);
		this.giant.setInvulnerable(true);
		this.giant.setAI(false);
		this.giant.setPersistent(true);
	}

	public Slime getSlime() {
		return this.slime;
	}
	
	@SuppressWarnings("deprecation")
	public void createNewSlime() {
		this.slime.remove();
		this.slime = (Slime) this.giant.getWorld().spawnEntity(this.giant.getLocation().add(0D, 8D, 0D), EntityType.SLIME);
		this.slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		this.slime.setSize(5);
		this.slime.setCustomNameVisible(false);
		this.slime.setCanPickupItems(false);
		this.slime.setPersistent(true);
		this.slime.setAI(false);
		this.slime.setGravity(false);
		this.slime.setMaxHealth(20);
		this.slime.setHealth(slime.getMaxHealth());
	}

	public Zombie getZombie() {
		return this.zombie;
	}

	@SuppressWarnings("deprecation")
	public void createNewZombie() {
		this.zombie.remove();
		this.zombie = (Zombie) this.giant.getWorld().spawnEntity(this.giant.getLocation(), EntityType.ZOMBIE);
		this.zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		this.zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999999, 1, false, false));
		this.zombie.setSilent(true);
		this.zombie.setCustomNameVisible(false);
		this.zombie.setCanPickupItems(false);
		this.zombie.setInvulnerable(true);
		this.zombie.setTarget(null);
		this.zombie.setPersistent(true);
	}

	public void syncEntities() {
		for (int i = 1; i <= 10; i++) {
			if (this.zombie.getLocation().add(0D, i, 0D).getBlock() != null && this.zombie.getLocation().add(0D, i, 0D).getBlock().getType() != Material.AIR) {
				this.zombie.teleport(slime.getLocation().add(0D, -8D, 0D));
				break;
			}
		}
		this.giant.teleport(this.zombie);
		double newX;
		double newZ;
		float nang = this.giant.getLocation().getYaw() + 90;

		if (nang < 0) {
			nang += 360;
		}

		newX = Math.cos(Math.toRadians(nang));
		newZ = Math.sin(Math.toRadians(nang));

		Location loc = new Location(this.giant.getWorld(), this.giant.getLocation().getX() - newX, this.giant.getLocation().getY(),
				this.giant.getLocation().getZ() - newZ, this.giant.getLocation().getYaw(), this.giant.getLocation().getPitch());
		
		this.slime.teleport(loc.add(0D, 8D, 0D));
		if (this.grabbedPlayer != null && Bukkit.getPlayer(this.grabbedPlayer) != null) {
			Bukkit.getPlayer(this.grabbedPlayer).teleport(this.zombie.getLocation().add(0D, 7D, 0D));
		}
	}
	
	public void grabPlayer(UUID uuid) {
		int count = 0;
		grabTaskID = new BukkitRunnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		}.runTaskTimer(AttackOnTitan.getInstance(), 0L, 20L).getTaskId();
	}

	public void remove() {
		if (grabTaskID != -1) {
			Bukkit.getScheduler().cancelTask(grabTaskID);
		}
		this.slime.remove();
		this.giant.remove();
		this.zombie.remove();
	}

}
