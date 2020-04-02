package com.aotmc.attackontitan.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

	public static String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static void message(Player player, String message) {
		player.sendMessage(color(message));
	}
	
}
