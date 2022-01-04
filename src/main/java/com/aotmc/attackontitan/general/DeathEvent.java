package com.aotmc.attackontitan.general;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathEvent implements Listener {

	private ODMData odmData;

	public DeathEvent(ODMData odmData) {
		this.odmData = odmData;
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (event.getPlayer().getEquipment().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getPlayer().getEquipment().getLeggings(), "odm"))) {
			return;
		}

		Player player = event.getPlayer();

		if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(player.getUniqueId())) {
			odmData.getWearingODM().get(player.getUniqueId()).remove();
			odmData.getWearingODM().remove(player.getUniqueId());
		}

		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
			player.addPassenger(armorStand);
			odmData.getWearingODM().put(player.getUniqueId(), armorStand);
		}, 5L);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (odmData.getWearingODM() == null || !odmData.getWearingODM().containsKey(event.getEntity().getUniqueId())) {
			return;
		}
		
		Player player = event.getEntity();
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			odmData.getWearingODM().get(player.getUniqueId()).remove();
			odmData.getWearingODM().remove(player.getUniqueId());
		}, 5L);
	}

}
