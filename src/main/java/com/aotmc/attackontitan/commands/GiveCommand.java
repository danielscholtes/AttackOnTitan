package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.swords.FirstGenBlade;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GiveCommand
{
    @Command(permission = "aot.command.give", aliases = {"give"}, usage = "give <target> (item)", requiredArgs = 2)
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {
        final Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null)
        {
            sender.sendMessage("Invalid target");
            return;
        }

        final String item = args[1];

        if (item.equalsIgnoreCase("blade"))
        {
            if (target.getInventory().firstEmpty() == -1)
            {
                sender.sendMessage("Targets inv is full");
                return;
            }

            final FirstGenBlade blade = new FirstGenBlade();

            final ItemBuilder builder = new ItemBuilder(blade.getMaterial())
                    .withName(blade.getName())
                    .withNBTString("id", blade.getId())
                    .withNBTString("level", String.valueOf(blade.getLevel()));

            target.getInventory().addItem(builder.getItem());
        }
    }

}
