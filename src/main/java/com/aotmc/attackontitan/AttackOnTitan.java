package com.aotmc.attackontitan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import com.aotmc.attackontitan.materials.PlayerListener;
import com.aotmc.attackontitan.upgrade.UpgradeCommand;
import com.aotmc.attackontitan.general.*;
import com.aotmc.attackontitan.materials.MaterialData;
import com.aotmc.attackontitan.odmgear.listeners.*;
import com.aotmc.attackontitan.titans.*;
import com.aotmc.attackontitan.upgrade.UpgradeListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.aotmc.attackontitan.commands.manager.CommandsManager;
import com.aotmc.attackontitan.general.util.TabComplete;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.music.Music;
import com.aotmc.attackontitan.music.MusicPlayer;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.odmgear.equip.ArmorListener;
import com.aotmc.attackontitan.skills.listeners.SpinningSlashActivate;
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
	private MusicPlayer musicPlayer;
	private MaterialData materialData;
	private static Economy econ = null;

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

		if (!setupEconomy() ) {
			Logger.getLogger("Minecraft").severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		instance = this;
		// Registers all events
		titanData = new TitanData(this);
		CommandsManager manager = new CommandsManager(this);
		odmData = new ODMData(this);
		materialData = new MaterialData(this);
		
		musicPlayer = new MusicPlayer();

		getServer().getPluginManager().registerEvents(new SpinningSlashActivate(this), this);
		getServer().getPluginManager().registerEvents(new ODMGearActivate(this, odmData), this);
		getServer().getPluginManager().registerEvents(new ODMLaunch(this, odmData), this);
		getServer().getPluginManager().registerEvents(new LogoutEvents(odmData, titanData, musicPlayer), this);
		getServer().getPluginManager().registerEvents(new TitanEvents(titanData, odmData, materialData), this);
		getServer().getPluginManager().registerEvents(new BoostListener(odmData), this);
		getServer().getPluginManager().registerEvents(new ODMGearEquip(odmData), this);
		getServer().getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
		getServer().getPluginManager().registerEvents(new JoinEvents(odmData), this);
		getServer().getPluginManager().registerEvents(new DeathEvent(odmData), this);
		getServer().getPluginManager().registerEvents(new ArmorStandEvents(odmData), this);
		getServer().getPluginManager().registerEvents(new WorldChangeListener(musicPlayer), this);
		getServer().getPluginManager().registerEvents(new TeleportListener(odmData, titanData), this);
		getServer().getPluginManager().registerEvents(new TitanSpawning(titanData), this);
		getServer().getPluginManager().registerEvents(new UpgradeListener(materialData), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(materialData), this);
		getServer().getPluginManager().registerEvents(new GasCanisterListener(), this);

		getCommand("upgrade").setExecutor(new UpgradeCommand());

		// Start all tasks
		titanData.startFollowTask();
		titanData.startPlayerDetectionTask();
		odmData.startBoostTask();
		odmData.startPreventFlyTask();
		odmData.startAlignODMTask();
		musicPlayer.startMusicPlayer();
		
		manager.registerCommand();
		getCommand("aot").setTabCompleter(new TabComplete());
		
		Bukkit.getScheduler().runTaskLater(this, () -> {
			for (World world : Bukkit.getWorlds()) {
				for (Entity entity : world.getEntities()) {
					if (!(entity instanceof Slime) && !(entity instanceof Zombie) && !(entity instanceof Giant)) {
						continue;
					}
					if (titanData.getTitans() != null && titanData.getTitans().containsKey(entity.getEntityId())) {
						titanData.getTitans().get(entity.getEntityId()).remove();
						titanData.getTitans().remove(entity.getEntityId());
						continue;
					}

					entity.remove();
				}
			}
		}, 10L);

		Bukkit.getScheduler().runTaskLater(this, () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				materialData.loadData(player.getUniqueId());

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
		}, 5L);

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> materialData.saveAllData(), 5L, 20 * 60 * 30L);
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
		
		for (UUID uuid : musicPlayer.getListeningToMusic().keySet()) {
			if (musicPlayer.getPlayerMusicTask().containsKey(uuid)) {
				Bukkit.getScheduler().cancelTask(musicPlayer.getPlayerMusicTask().get(uuid));
			}
			
			if (Bukkit.getPlayer(uuid) == null) {
				continue;
			}
			
			for (Music music : Music.values()) {
				Bukkit.getPlayer(uuid).stopSound(music.getName(), SoundCategory.RECORDS);
			}
		}

		materialData.saveAllData();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
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
