package com.aotmc.attackontitan.odmgear.listeners;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;

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

	@EventHandler
	public void onShootHook(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if (event.getAction() == Action.PHYSICAL) {
			return;
		}

		if (!event.getPlayer().isSneaking()) {
			return;
		}

		boolean left = (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK);

		if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
			return;
		}

		disableODM(player, false);
		if (data.getPlayerHooks().containsKey(player.getUniqueId())) {
			for (Hook playerHook : data.getPlayerHooks().get(player.getUniqueId())) {
				if ((playerHook.isLeft() && left) || (!playerHook.isLeft() && !left)) {
					playerHook.remove();
					data.getHooks().remove(playerHook.getHookID());
				}
			}
			if (data.getPlayerHooks().get(player.getUniqueId()).isEmpty()) {
				data.getPlayerHooks().remove(player.getUniqueId());
			}
		}
		if (left && data.getLocationHookLeft().containsKey(player.getUniqueId())) {
			data.getLocationHookLeft().remove(player.getUniqueId());
		}
		if (!left && data.getLocationHookRight().containsKey(player.getUniqueId())) {
			data.getLocationHookRight().remove(player.getUniqueId());
		}

		activateHook(player, false, left, true);
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

		if (data.getLastODMActivate().containsKey(player.getUniqueId()) && data.getLastODMActivate().get(player.getUniqueId()) > System.currentTimeMillis()) {
			return;
		}

		disableODM(player, true);
		
		/*
		 * Activates the ODM Gear
		 */
		event.setCancelled(true);
		RayTraceResult rayTrace = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().toVector().normalize(), 70, 15, (e) -> (e.getType() == EntityType.GIANT || e.getType() == EntityType.SLIME));
		if (rayTrace != null && rayTrace.getHitEntity() != null) {
			activateHook(player, false, true, false);
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				activateHook(player, false, false, false);
			}, 2L);
			return;
		}
		activateHook(player, true, true, false);
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			activateHook(player, true, false, false);
		}, 2L);
		return;
	}

	/**
	 * If player is in the process of shooting hooks, cancel that and remove him from all neccessary
	 * lists and maps
	 */
	private void disableODM(Player player, boolean bothHooks) {
		if (bothHooks) {
			if (data.getPlayerHooks().containsKey(player.getUniqueId())) {
				for (Hook playerHook : data.getPlayerHooks().get(player.getUniqueId())) {
					playerHook.remove();
					data.getHooks().remove(playerHook.getHookID());
				}
				data.getPlayerHooks().remove(player.getUniqueId());
			}
			if (data.getLocationHookLeft().containsKey(player.getUniqueId())) {
				data.getLocationHookLeft().remove(player.getUniqueId());
			}
			if (data.getLocationHookRight().containsKey(player.getUniqueId())) {
				data.getLocationHookRight().remove(player.getUniqueId());
			}
		}
		if (data.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(data.getPlayerTasksLanding().get(player.getUniqueId()));
			data.getPlayerTasksLanding().remove(player.getUniqueId());
		}
		if (data.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(data.getPlayerTasksEffect().get(player.getUniqueId()));
			data.getPlayerTasksEffect().remove(player.getUniqueId());
		}
	}

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
	private void activateHook(Player player, boolean wide, boolean left, boolean fromPlayer) {
		data.getLastODMActivate().put(player.getUniqueId(), System.currentTimeMillis() + 200);

		Hook hook = new Hook(player.getUniqueId(), left, plugin);
		if (data.getPlayerHooks().containsKey(player.getUniqueId())) {
			data.getPlayerHooks().get(player.getUniqueId()).add(hook);
		} else {
			Set<Hook> set = new HashSet<Hook>();
			set.add(hook);
			data.getPlayerHooks().put(player.getUniqueId(), set);
		}
		data.getHooks().put(hook.getHookID(), hook);
		player.playSound(player.getLocation(), "odmgear", 0.5F, 1F);
		hook.launchHook(fromPlayer, wide);
	}

}
