package com.aotmc.attackontitan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.command.Command;

public class GasCanisterCommand {

    @Command(permission = "aot.command.give.gascanister", aliases = {"gascanister"}, usage = "<give> <target>", requiredArgs = 2)
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args) {
        final String argument = args[0];
        final Player target = args.length == 2 ? Bukkit.getPlayerExact(args[1]) : null;

        if (target == null || !target.isOnline()) {
            sender.sendMessage(Utils.color("&cAdmin &8» '&c" + target + "&8' is not a valid target!"));
            return;
        }

        if (argument.equalsIgnoreCase("give")) {
            target.getInventory().addItem(Utils.createNewGasCanister(250));
        }
    }
}
