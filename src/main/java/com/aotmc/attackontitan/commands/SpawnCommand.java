package com.aotmc.attackontitan.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.titans.Titan;
import com.aotmc.attackontitan.titans.TitanData;
import com.aotmc.attackontitan.titans.TitanType;
import com.codeitforyou.lib.api.command.Command;

public class SpawnCommand {
	
	private static TitanData titanData = AttackOnTitan.getTitanData();
	
	@Command(permission = "aot.command.spawn", aliases = { "spawn" }, usage = "<titan>", requiredArgs = 1)
	public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args) {

		final String titanType = args[0].toUpperCase();

		if (!Arrays.asList("SMALL", "MEDIUM", "LARGE").contains(titanType)) {
			sender.sendMessage("Invalid type");
			return;
		}

		sender.sendMessage(Utils.color("&cAdmin &8» &7You have spawned a &2" + titanType + " &7Titan!"));
		switch (titanType) {
			case "SMALL":
				new Titan(sender.getLocation(), TitanType.SMALL, titanData);
				return;
			case "MEDIUM":
				new Titan(sender.getLocation(), TitanType.MEDIUM, titanData);
				return;
			case "LARGE":
				new Titan(sender.getLocation(), TitanType.LARGE, titanData);
				return;
			default:
				return;
		}
	}
	
}
