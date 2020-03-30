package com.aotmc.attackontitan;

import org.bukkit.plugin.java.JavaPlugin;

import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.odmgear.listeners.ODMGearActivate;
import com.aotmc.attackontitan.odmgear.listeners.ODMLaunch;
import com.aotmc.attackontitan.odmgear.listeners.ODMLogout;
import com.aotmc.attackontitan.skills.listeners.SpinningSlashActivate;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class AttackOnTitan extends JavaPlugin {
	
	private static AttackOnTitan instance;
	private ProtocolManager protocolManager;
	private ODMData odmData = new ODMData();
	
	public void onLoad() {
		protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new SpinningSlashActivate(this), this);
		getServer().getPluginManager().registerEvents(new ODMGearActivate(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLaunch(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLogout(odmData), this);
		
	}
	
	public static AttackOnTitan getInstance() {
		return instance;
	}
	
	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}
	
}
