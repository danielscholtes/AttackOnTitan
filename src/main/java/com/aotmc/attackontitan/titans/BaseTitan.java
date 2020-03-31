package com.aotmc.attackontitan.titans;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

public interface BaseTitan {
	
	public void spawnTitan(Location spawnLocation);
	public Zombie getZombie();
	public ArmorStand getArmorStand();
	public Slime getSlime();
	public void syncEntities();

}
