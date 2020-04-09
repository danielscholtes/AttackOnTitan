package com.aotmc.attackontitan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.aotmc.attackontitan.commands.listener.ConverterListener;
import com.aotmc.attackontitan.commands.manager.CommandsManager;
import com.aotmc.attackontitan.general.ArmorStandEvents;
import com.aotmc.attackontitan.general.JoinEvents;
import com.aotmc.attackontitan.general.LogoutEvents;
import com.aotmc.attackontitan.general.util.TabComplete;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.odmgear.equip.ArmorListener;
import com.aotmc.attackontitan.odmgear.listeners.BoostListener;
import com.aotmc.attackontitan.odmgear.listeners.ODMGearActivate;
import com.aotmc.attackontitan.odmgear.listeners.ODMGearEquip;
import com.aotmc.attackontitan.odmgear.listeners.ODMLaunch;
import com.aotmc.attackontitan.skills.listeners.SpinningSlashActivate;
import com.aotmc.attackontitan.titans.Titan;
import com.aotmc.attackontitan.titans.TitanData;
import com.aotmc.attackontitan.titans.TitanEvents;
import com.aotmc.attackontitan.titans.TitanZombieFire;
import com.codeitforyou.lib.api.item.ItemUtil;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class AttackOnTitan extends JavaPlugin {

	private Random random = new Random();
	private static AttackOnTitan instance;
	private ProtocolManager protocolManager;
	private final CommandsManager manager = new CommandsManager(this);
	private static TitanData titanData;
	private ODMData odmData;

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
		odmData = new ODMData(this);
		titanData.startFollowTask();
		titanData.startPlayerDetectionTask();
		odmData.startBoostTask();
		odmData.startPreventFlyTask();
		odmData.startAlignODMTask();
		
		getServer().getPluginManager().registerEvents(new TitanZombieFire(), this);
		getServer().getPluginManager().registerEvents(new SpinningSlashActivate(this), this);
		getServer().getPluginManager().registerEvents(new ODMGearActivate(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLaunch(this, odmData), this);
		getServer().getPluginManager().registerEvents(new LogoutEvents(odmData, titanData), this);
		getServer().getPluginManager().registerEvents(new TitanEvents(titanData), this);
		getServer().getPluginManager().registerEvents(new BoostListener(odmData), this);
		getServer().getPluginManager().registerEvents(new ConverterListener(), this);
		getServer().getPluginManager().registerEvents(new ODMGearEquip(odmData), this);
		getServer().getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
		getServer().getPluginManager().registerEvents(new JoinEvents(odmData), this);
		getServer().getPluginManager().registerEvents(new ArmorStandEvents(odmData), this);
		
		manager.registerCommand();
		getCommand("aot").setTabCompleter(new TabComplete());
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getGameMode() == GameMode.SURVIVAL) {
						player.setAllowFlight(false);
					}
					
					if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(player.getUniqueId())) {
						continue;
					}

					if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
						continue;
					}
					
					ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
					odmData.getWearingODM().put(player.getUniqueId(), armorStand);
					player.addPassenger(armorStand);
					player.setAllowFlight(true);
				}
			}
		}, 5L);
	}
	
	public void onDisable() {
		if (titanData.getTitans() != null) {
			for (Titan titan : titanData.getTitans().values()) {
				titan.remove();
			}
			
			titanData.getTitans().clear();
		}
		
		if (odmData.getHooks() != null) {
			for (Hook hook : odmData.getHooks().values()) {
				hook.remove();
			}
			odmData.getHooks().clear();
		}
		
		if (odmData.getWearingODM() != null) {
			Iterator<UUID> iterator = odmData.getWearingODM().keySet().iterator();
			
			while (iterator.hasNext()) {
				UUID uuid = iterator.next();
				if (Bukkit.getPlayer(uuid) != null) {
					if (Bukkit.getPlayer(uuid).getGameMode() == GameMode.SURVIVAL) {
						Bukkit.getPlayer(uuid).setAllowFlight(false);
						Bukkit.getPlayer(uuid).setFlying(false);
					}
				}
				ArmorStand armorStand = odmData.getWearingODM().get(uuid);
				iterator.remove();
				armorStand.remove();
			}
			odmData.getWearingODM().clear();
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
	
	public Random getRandom() {
		return random;
	}

}
