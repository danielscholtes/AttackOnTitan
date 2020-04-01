package com.aotmc.attackontitan.titans;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class Titan {
	
	private Giant giant;
	private Zombie zombie;
	private Slime slime;
	private int size;
	private TitanType titanType;

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
		zombie.setRemoveWhenFarAway(false);
		zombie.setPersistent(true);
		
		giant = (Giant) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.GIANT);
		giant.setSilent(true);
		giant.setCustomNameVisible(true);
		giant.setCustomName(ChatColor.translateAlternateColorCodes('&', "&6" + size + " Meter Titan"));
		giant.setCanPickupItems(false);
		giant.setInvulnerable(true);
		giant.setAI(false);
		zombie.setRemoveWhenFarAway(false);
		giant.setPersistent(true);
		
		slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 8D, 0D), EntityType.SLIME);
		slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 0, false, false));
		slime.setSize(5);
		slime.setCustomNameVisible(false);
		slime.setCanPickupItems(false);
		slime.setPersistent(true);
		slime.setAI(false);
		zombie.setRemoveWhenFarAway(false);
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

	public Slime getSlime() {
		return this.slime;
	}

	public Zombie getZombie() {
		return this.zombie;
	}

	public void syncEntities() {
		for (int i = 1; i <= 10; i++) {
			if (this.zombie.getLocation().add(0D, i, 0D).getBlock() != null && this.zombie.getLocation().add(0D, i, 0D).getBlock().getType() != Material.AIR) {
				this.zombie.teleport(slime.getLocation().add(0D, -7.5D, 0D));
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
		
		this.slime.teleport(loc.add(0D, 7.5D, 0D));
	}

	public void remove() {
		this.slime.remove();
		this.giant.remove();
		this.zombie.remove();
	}

}
