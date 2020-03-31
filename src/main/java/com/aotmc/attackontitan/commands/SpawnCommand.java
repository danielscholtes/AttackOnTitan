package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.titans.SmallTitan;
import com.aotmc.attackontitan.titans.TitanData;
import com.codeitforyou.lib.api.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpawnCommand {
	
	private static TitanData titanData = AttackOnTitan.getTitanData();
	
	@Command(permission = "aot.command.spawn", aliases = { "spawn" }, usage = "<titan>", requiredArgs = 1)
	public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args) {

		final String titanType = args[0].toUpperCase();

		if (!Arrays.asList("SMALL", "MEDIUM", "LARGE").contains(titanType)) {
			sender.sendMessage("Invalid type");
			return;
		}

		sender.sendMessage("Spawned a " + titanType + " titan!");
		switch (titanType) {
			case "SMALL":
				titanData.getTitans().add(new SmallTitan(sender.getLocation()));
				return;
			case "MEDIUM":
				return;
			case "LARGE":
				return;
			default:
				return;
		}
	}
	
}
