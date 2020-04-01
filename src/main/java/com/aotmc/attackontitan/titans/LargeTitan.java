package com.aotmc.attackontitan.titans;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

public class LargeTitan implements BaseTitan {
	
	private Zombie zombie;
	private ArmorStand armorStand;
	private Slime slime;

	public LargeTitan(Location spawnLocation) {
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
		zombie.setTarget(null);
		zombie.setPersistent(true);
		
		armorStand = (ArmorStand) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 16D, 0D), EntityType.ARMOR_STAND);
		armorStand.setBasePlate(false);
		armorStand.setCanPickupItems(false);
		armorStand.setCustomNameVisible(false);
		armorStand.setInvulnerable(true);
		armorStand.setPersistent(true);
		armorStand.setGravity(false);
		armorStand.setCollidable(true);
		
		slime = (Slime) spawnLocation.getWorld().spawnEntity(spawnLocation.add(0D, 16D, 0D), EntityType.SLIME);
		slime.setSize(4);
		slime.setCustomNameVisible(false);
		slime.setCanPickupItems(false);
		slime.setPersistent(true);
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
		for (int i = 1; i <= 16; i++) {
			if (this.zombie.getLocation().add(0D, i, 0D).getBlock() != null && this.zombie.getLocation().add(0D, i, 0D).getBlock().getType() != Material.AIR) {
				this.zombie.teleport(slime.getLocation().add(0D, -16, 0D));
				break;
			}
		}
		this.armorStand.teleport(zombie.getLocation().add(0D, 16D, 0D));
		this.slime.teleport(zombie.getLocation().add(0D, 16D, 0D));
	}


	@Override
	public void remove() {
		this.armorStand.remove();
		this.slime.remove();
		this.zombie.remove();
	}

}
