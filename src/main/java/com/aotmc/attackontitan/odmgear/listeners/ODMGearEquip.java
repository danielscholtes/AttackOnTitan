package com.aotmc.attackontitan.odmgear.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.Hook;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.odmgear.equip.ArmorEquipEvent;
import com.codeitforyou.lib.api.item.ItemUtil;

import java.lang.reflect.InvocationTargetException;

public class ODMGearEquip implements Listener {
	
	private ODMData data;
	
	public ODMGearEquip(ODMData data) {
		this.data = data;
	}
	
	@EventHandler
	public void onEquip(ArmorEquipEvent event) {
		Player player = event.getPlayer();
		if (event.getOldArmorPiece() != null && Boolean.valueOf(ItemUtil.getNBTString(event.getOldArmorPiece(), "odm"))) {
			event.setUpdateOld(true);
			int tier = Integer.parseInt(ItemUtil.getNBTString(event.getOldArmorPiece(), "tier"));
			int gas = Integer.parseInt(ItemUtil.getNBTString(event.getOldArmorPiece(), "gas"));
			event.setOldArmorPiece(Utils.createODMLeggings(tier, gas));
			if (event.getNewArmorPiece() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
				if (data.getWearingODM().containsKey(player.getUniqueId())) {
					data.getWearingODM().get(player.getUniqueId()).remove();
					data.getWearingODM().remove(player.getUniqueId());
				}
				if (data.getLastODMActivate().containsKey(player.getUniqueId())) {
					data.getLastODMActivate().remove(player.getUniqueId());
					return;
				}
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
				if (data.getPlayerTasksLanding().containsKey(player.getUniqueId())) {
					Bukkit.getScheduler().cancelTask(data.getPlayerTasksLanding().get(player.getUniqueId()));
					data.getPlayerTasksLanding().remove(player.getUniqueId());
				}
				if (data.getPlayerTasksEffect().containsKey(player.getUniqueId())) {
					Bukkit.getScheduler().cancelTask(data.getPlayerTasksEffect().get(player.getUniqueId()));
					data.getPlayerTasksEffect().remove(player.getUniqueId());
				}
				if (player.getGameMode() == GameMode.SURVIVAL) {
					player.setAllowFlight(false);
					player.setFlying(false);
				}
				int newTier = Integer.parseInt(ItemUtil.getNBTString(event.getNewArmorPiece(), "tier"));
				int newGas = Integer.parseInt(ItemUtil.getNBTString(event.getNewArmorPiece(), "gas"));

				Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> player.getEquipment().setLeggings(Utils.createODMHoe(newTier, newGas)), 2L);
				return;
			}
			return;
		}
		if (event.getNewArmorPiece() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getNewArmorPiece(), "odm"))) {
			return;
		}
		
		player.setAllowFlight(true);

		int tier = Integer.parseInt(ItemUtil.getNBTString(event.getNewArmorPiece(), "tier"));
		int gas = Integer.parseInt(ItemUtil.getNBTString(event.getNewArmorPiece(), "gas"));

		ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
		player.addPassenger(armorStand);
		data.getWearingODM().put(player.getUniqueId(), armorStand);
		event.setNewArmorPiece(new ItemStack(Material.AIR));
		event.setUpdateNew(true);

		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> player.getEquipment().setLeggings(Utils.createODMHoe(tier, gas)), 2L);
	}

}
