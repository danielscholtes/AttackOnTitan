package com.aotmc.attackontitan.blades;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BladeOffHandEvent implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Bukkit.broadcastMessage("a");
	}

}
