package com.aotmc.attackontitan.commands;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.titans.Titan;
import com.aotmc.attackontitan.titans.TitanData;
import com.aotmc.attackontitan.titans.TitanType;
import com.codeitforyou.lib.api.command.Command;

public class SpawnCommand {
	
	private static TitanData titanData = AttackOnTitan.getTitanData();
	private static Random rand = new Random();
	
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
				Titan smallTitan = new Titan(sender.getLocation(), TitanType.SMALL, rand.nextInt((6 - 2) + 1) + 2, titanData);
				titanData.getTitans().put(smallTitan.getSlime().getEntityId(), smallTitan);
				return;
			case "MEDIUM":
				Titan mediumTitan = new Titan(sender.getLocation(), TitanType.MEDIUM, rand.nextInt((10 - 7) + 1) + 7, titanData);
				titanData.getTitans().put(mediumTitan.getSlime().getEntityId(), mediumTitan);
				return;
			case "LARGE":
				Titan largeTitan = new Titan(sender.getLocation(), TitanType.LARGE, rand.nextInt((15 - 11) + 1) + 11, titanData);
				titanData.getTitans().put(largeTitan.getSlime().getEntityId(), largeTitan);
				return;
			default:
				return;
		}
	}
	
}
