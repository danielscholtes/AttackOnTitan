package com.aotmc.attackontitan;

import org.bukkit.plugin.java.JavaPlugin;

import com.aotmc.attackontitan.skills.listeners.SpinningSlashInteract;

public class AttackOnTitan extends JavaPlugin {
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new SpinningSlashInteract(this), this);
		
	}
	
}
