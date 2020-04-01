package com.aotmc.attackontitan;

import com.aotmc.attackontitan.util.TabComplete;
import org.bukkit.plugin.java.JavaPlugin;

import com.aotmc.attackontitan.commands.CommandsManager;
import com.aotmc.attackontitan.commands.gui.listener.InventoryClickListener;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.odmgear.listeners.ODMGearActivate;
import com.aotmc.attackontitan.odmgear.listeners.ODMLaunch;
import com.aotmc.attackontitan.odmgear.listeners.ODMLogout;
import com.aotmc.attackontitan.skills.listeners.SpinningSlashActivate;
import com.aotmc.attackontitan.titans.Titan;
import com.aotmc.attackontitan.titans.TitanData;
import com.aotmc.attackontitan.titans.TitanEvents;
import com.aotmc.attackontitan.titans.TitanZombieFire;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class AttackOnTitan extends JavaPlugin {

	private static AttackOnTitan instance;
	private ProtocolManager protocolManager;
	private final CommandsManager manager = new CommandsManager(this);
	private static TitanData titanData;

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
		titanData = new TitanData(this);
		CommandsManager manager = new CommandsManager(this);
		ODMData odmData = new ODMData(this);
		titanData.startFollowTask();
		titanData.startPlayerDetectionTask();
		getServer().getPluginManager().registerEvents(new TitanZombieFire(), this);
		getServer().getPluginManager().registerEvents(new SpinningSlashActivate(this), this);
		getServer().getPluginManager().registerEvents(new ODMGearActivate(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLaunch(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLogout(odmData), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new TitanEvents(titanData), this);

		manager.registerCommand();
		getCommand("aot").setTabCompleter(new TabComplete());
	}
	
	public void onDisable() {
		if (titanData.getTitans() != null) {
			for (Titan titan : titanData.getTitans().values()) {
				titan.remove();
			}
			
			titanData.getTitans().clear();
		}
	}

	/**
	 * Returns the instance of the main plugin
	 *
	 * @return		instance of main plugin
	 */
	public static AttackOnTitan getInstance() {
		return instance;
	}
	
	public static TitanData getTitanData() {
		return titanData;
	}

	/**
	 * Returns the protocol manager
	 *
	 * @return		protocol manager
	 */
	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	public CommandsManager getManager()
	{
		return manager;
	}

}
