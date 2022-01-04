package com.aotmc.attackontitan.odmgear.listeners;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.aotmc.attackontitan.titans.TitanData;
import com.codeitforyou.lib.api.item.ItemUtil;
import net.ess3.api.events.teleport.PreTeleportEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.olzie.playerwarps.api.PlayerWarpTeleportEvent;


public class TeleportListener implements Listener {

	private ODMData odmData;
	private TitanData titanData;

	public TeleportListener(ODMData odmData, TitanData titanData) {
		this.odmData = odmData;
		this.titanData = titanData;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreTeleport(PreTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getTeleportee().getBase();
		if (player.getEquipment().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getEquipment().getLeggings(), "odm"))) {
			return;
		}

		if (titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
			event.setCancelled(true);
			return;
		}

		if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(player.getUniqueId())) {
			odmData.getWearingODM().get(player.getUniqueId()).remove();
			odmData.getWearingODM().remove(player.getUniqueId());
		}
		event.setCancelled(true);
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			player.teleport(event.getTarget().getLocation());
		}, 3L);
	}

	/**
	 * PlayerWarps plugin
	 *
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreTeleport(PlayerWarpTeleportEvent event) {
		Player player = event.getTeleporter();
		if (player.getEquipment().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getEquipment().getLeggings(), "odm"))) {
			return;
		}

		if (titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
			event.setCancelled(true);
			return;
		}

		if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(player.getUniqueId())) {
			odmData.getWearingODM().get(player.getUniqueId()).remove();
			odmData.getWearingODM().remove(player.getUniqueId());
		}
	}
	 */

	@EventHandler(priority = EventPriority.LOWEST)
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		if (player.getEquipment().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getEquipment().getLeggings(), "odm"))) {
			return;
		}

		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
			odmData.getWearingODM().put(player.getUniqueId(), armorStand);
			for (Entity entity : player.getPassengers()) {
				entity.remove();
			}
			player.addPassenger(armorStand);
		}, 3L);

	}
	
}
