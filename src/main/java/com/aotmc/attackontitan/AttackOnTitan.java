package com.aotmc.attackontitan;

import org.bukkit.plugin.java.JavaPlugin;

import com.aotmc.attackontitan.skills.listeners.SpinningSlashActivate;

public class AttackOnTitan extends JavaPlugin {
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new SpinningSlashActivate(this), this);
		
	}
	
}
