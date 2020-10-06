package com.aotmc.attackontitan.odmgear.listeners;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.codeitforyou.lib.api.item.ItemUtil;

public class ODMGearActivate implements Listener {
	
	private AttackOnTitan plugin;
	private ODMData data;
	
	public ODMGearActivate(AttackOnTitan plugin, ODMData data) {
		this.plugin = plugin;
		this.data = data;
	}
	
	/**
	 * Activates the ODM gear by player swapping hand items with the hotkey
	 */
	@EventHandler
	public void onActivate(PlayerSwapHandItemsEvent event) {
		
		Player player = event.getPlayer();
		/*
		 * Checks if player is wearing ODM gear
		 * Gear will have a proper check with NBT soon
		 */
		if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
			return;
		}
		
		/*
		 * If player is in the process of shooting hooks, cancel that and remove him from all neccessary
		 * lists and maps
		 */
		
		if (data.getLastODMActivate().containsKey(player.getUniqueId()) && data.getLastODMActivate().get(player.getUniqueId()) > System.currentTimeMillis()) {
			return;
		}	
		if (data.getAttachedHook().contains(player.getUniqueId())) {
			data.getAttachedHook().remove(player.getUniqueId());
		}
		if (data.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : data.getPlayerHooks().get(player.getUniqueId())) {
				playerHook.remove();
				data.getHooks().remove(playerHook.getHookID());
			}
			data.getPlayerHooks().remove(player.getUniqueId());
		}
		if (data.getLocationHooks().containsKey(player.getUniqueId())) {
			data.getLocationHooks().remove(player.getUniqueId());
		}
		if (data.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(data.getPlayerTasksLanding().get(player.getUniqueId()));
			data.getPlayerTasksLanding().remove(player.getUniqueId());
		}
		if (data.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(data.getPlayerTasksEffect().get(player.getUniqueId()));
			data.getPlayerTasksEffect().remove(player.getUniqueId());
		}
		
		/*
		 * Activates the ODM Gear
		 */
		event.setCancelled(true);
		RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().toVector().normalize(), 70, 15, (e) -> (e.getType() == EntityType.GIANT || e.getType() == EntityType.SLIME));
		if (rayTrace != null && rayTrace.getHitEntity() != null) {
			activateGear(player, false);
			return;
		}
		activateGear(player, true);
	}
	
	/*
	@EventHandler
	public void onSwitchMode(PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND || (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		Player player = event.getPlayer();
		
		if (!event.getPlayer().isSneaking()) {
			return;
		}
		
		if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
			return;
		}
		
		if (data.getNarrowODM().contains(player.getUniqueId())) {
			data.getNarrowODM().remove(player.getUniqueId());
			Utils.message(player, "&aSwitching to wide mode");
			return;
		}
		data.getNarrowODM().add(player.getUniqueId());
		Utils.message(player, "&aSwitching to narrow mode");
	}*/

	/**
	 * Prevents silverfish from taking damage
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() == null) {
			return;
		}
		
		if (!(event.getEntity() instanceof Silverfish)) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	/**
	 * Actives the ODM Gear of the specified player
	 * 
	 * @param		player to activate gear for
	 */
	private void activateGear(Player player, boolean wide) {
		data.getLastODMActivate().put(player.getUniqueId(), System.currentTimeMillis() + 200);
		/*
		 * Creates hooks and launches them
		 */
		Hook hookLeft = new Hook(player.getUniqueId(), true, plugin);
		Hook hookRight = new Hook(player.getUniqueId(), false, plugin);
		Set<Hook> set = new HashSet<Hook>();
		set.add(hookLeft);
		set.add(hookRight);
		data.getPlayerHooks().put(player.getUniqueId(), set);
		data.getHooks().put(hookRight.getHookID(), hookRight);
		data.getHooks().put(hookLeft.getHookID(), hookLeft);
		player.playSound(player.getLocation(), "odmgear", 0.5F, 1F);
		hookRight.launchHook(wide);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				hookLeft.launchHook(wide);
			}
		}, 2L);
	}

}
