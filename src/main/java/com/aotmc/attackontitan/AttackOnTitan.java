package com.aotmc.attackontitan;

import com.aotmc.attackontitan.commands.CommandsManager;
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

	/**
	 * Runs when server is loaded
	 */
	public void onLoad() {
		// Loads ProtocolManager
		protocolManager = ProtocolLibrary.getProtocolManager();
	}

	/**
	 * Runs when server is enabled
	 */
	public void onEnable() {
		instance = this;
		// Registers all events
		getServer().getPluginManager().registerEvents(new SpinningSlashActivate(this), this);
		getServer().getPluginManager().registerEvents(new ODMGearActivate(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLaunch(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLogout(odmData), this);

		new CommandsManager(this).registerCommand();
	}

	/**
	 * Returns the instance of the main plugin
	 *
	 * @return		instance of main plugin
	 */
	public static AttackOnTitan getInstance() {
		return instance;
	}

	/**
	 * Returns the protocol manager
	 *
	 * @return		protocol manager
	 */
	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}

}
