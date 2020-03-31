package com.aotmc.attackontitan.titans;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

public class SmallTitan implements BaseTitan {
	
	private Zombie zombie;
	private ArmorStand armorStand;
	private Slime slime;

	public SmallTitan(Location spawnLocation) {
		spawnTitan(spawnLocation);
	}


	@SuppressWarnings("deprecation")
	@Override
	public void spawnTitan(Location spawnLocation) {
		zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
		zombie.setSilent(true);
		zombie.setCustomNameVisible(false);
		zombie.setCanPickupItems(false);
		zombie.setInvulnerable(true);
		zombie.setBaby(false);
		zombie.setPersistent(false);
		
		armorStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 2D, 0D), EntityType.ARMOR_STAND);
		armorStand.setBasePlate(false);
		armorStand.setCanPickupItems(false);
		armorStand.setCustomNameVisible(false);
		armorStand.setInvulnerable(true);
		armorStand.setPersistent(false);
		armorStand.setGravity(false);
		armorStand.setCollidable(true);
		
		slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 3D, 0D), EntityType.SLIME);
		slime.setSize(2);
		slime.setCustomNameVisible(false);
		slime.setCanPickupItems(false);
		slime.setInvulnerable(true);
		slime.setPersistent(false);
		slime.setAI(false);
		slime.setGravity(false);
	}
	
	@Override
	public Zombie getZombie() {
		return this.zombie;
	}

	@Override
	public ArmorStand getArmorStand() {
		return this.armorStand;
	}

	@Override
	public Slime getSlime() {
		return this.slime;
	}


	@Override
	public void syncEntities() {
		this.armorStand.teleport(zombie.getLocation().add(0D, 2D, 0D));
		this.slime.teleport(zombie.getLocation().add(0D, 3D, 0D));
	}

}
