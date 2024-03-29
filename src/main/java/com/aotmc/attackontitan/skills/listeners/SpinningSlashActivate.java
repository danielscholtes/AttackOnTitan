package com.aotmc.attackontitan.skills.listeners;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.item.ItemUtil;

public class SpinningSlashActivate implements Listener {

	private AttackOnTitan plugin;
	
	public SpinningSlashActivate(AttackOnTitan plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Handles player right clicking for spinning slash
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		/*
		 * Checks if player is right-cliking
		 */
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		/*
		 * Checks if player is using their main hand
		 */
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		if (event.getPlayer().isSneaking()) {
			return;
		}
		
		/*
		 * Checks if item is a diamond sword
		 */
		if (event.getItem() == null) {
			return;
		}
		
		if (!event.getPlayer().hasPermission("skill.spin")) {
			return;
		}

		if (!Boolean.valueOf(ItemUtil.getNBTString(event.getItem(), "blade"))) {
			return;
		}
        
		Player player = event.getPlayer();
		player.sendMessage(Utils.color("&7You have activated the &2Spinning Slash &7skill."));
		
		/*
		 * Makes player spin and applies speed for an awesome look
		 */
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		DataWatcher dataWatcher = entityPlayer.getDataWatcher();
		dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
		player.setVelocity(player.getEyeLocation().getDirection().multiply(2.5));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 11, 8, false, false, false));
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x00);
			}
		}, 10L);
	}

}
