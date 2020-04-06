package com.aotmc.attackontitan.general;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.odmgear.ODMData;
import com.codeitforyou.lib.api.item.ItemUtil;

public class JoinEvents implements Listener {
	
	private ODMData odmData;
	
	public JoinEvents(ODMData odmData) {
		this.odmData = odmData;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (event.getPlayer().getEquipment().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getPlayer().getEquipment().getLeggings(), "odm"))) {
			return;
		}
		
		if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(event.getPlayer().getUniqueId())) {
			return;
		}
		
		Player player = event.getPlayer();
		
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				ArmorStand armorStand = Utils.createODMArmorStand(player.getLocation());
				player.addPassenger(armorStand);
				odmData.getWearingODM().put(player.getUniqueId(), armorStand);
			}
		}, 5L);
	}

}
