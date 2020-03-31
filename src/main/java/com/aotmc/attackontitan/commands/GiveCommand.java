package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.blades.Blades;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

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

        final String blade = args[1];

        if (target.getInventory().firstEmpty() == -1)
        {
            sender.sendMessage("Targets inv is full");
            return;
        }

        final Blades blades = Blades.valueOf(blade);

        final ItemBuilder builder = new ItemBuilder(Material.WOODEN_SWORD)
                .withName(blades.getFormattedName(blade))
                .withNBTString("durability", String.valueOf(blades.getDurability()))
                .withNBTString("damage", String.valueOf(blades.getDamage()))
                .withLore(blades.getDefaultLore(blade, blades.getDamage(), blades.getDurability()));

        final ItemMeta meta = builder.getItem().getItemMeta();

        if (meta != null)
        {
            meta.setCustomModelData(blades.getModelData());
            builder.getItem().setItemMeta(meta);
        }

        target.getInventory().addItem(builder.getItem());
    }

}
