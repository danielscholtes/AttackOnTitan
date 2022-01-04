package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.odmgear.ODMType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.command.Command;

import java.util.ArrayList;
import java.util.List;

public class ODMCommand {

    @Command(permission = "aot.command.give.odm", aliases = {"odm"}, usage = "<give> [target] (odm)", requiredArgs = 2)
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args) {
        final String argument = args[0];
        final Player target = args.length == 3 ? Bukkit.getPlayerExact(args[1]) : null;
        final String odm = args.length == 3 ? args[2] : null;
        final List<String> odmList = new ArrayList<>();

        for (ODMType odmType : ODMType.values()) {
            odmList.add(odmType.toString());
        }


        if (target == null || !target.isOnline()) {
            sender.sendMessage(Utils.color("&cAdmin &8» '&c" + target + "&8' is not a valid target!"));
            return;
        }

        if (argument.equalsIgnoreCase("give")) {
            if (!odmList.contains(odm.toUpperCase())) {
                sender.sendMessage(Utils.color("&cAdmin &8» '&f" + odm.toUpperCase() + "&8' &7is not a valid blade!"));
                return;
            }

            final ODMType odmType = ODMType.valueOf(odm.toUpperCase());
            target.getInventory().addItem(Utils.createODMLeggings(odmType.getHierarchy(), 0));
        }
    }
}
