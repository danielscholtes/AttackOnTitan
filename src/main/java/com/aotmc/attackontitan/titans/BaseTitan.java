package com.aotmc.attackontitan.titans;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;

public interface BaseTitan {
	
	void spawnTitan(Location spawnLocation);
	Zombie getZombie();
	ArmorStand getArmorStand();
	Slime getSlime();
	void syncEntities();
	void remove();

}
