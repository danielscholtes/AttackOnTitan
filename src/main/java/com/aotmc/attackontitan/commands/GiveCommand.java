package com.aotmc.attackontitan.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.blades.Blades;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.item.ItemBuilder;

public class GiveCommand
{

    @Command(permission = "aot.command.give", aliases = {"give"}, usage = "<target> (item)", requiredArgs = 2)
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {
        final Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null)
        {
            sender.sendMessage("Invalid target");
            return;
        }

        final String blade = args[1].toUpperCase();

        if (!Arrays.asList("FIRST_GEN_BLADE", "FIRST_GEN_BLADE_MK_II", "FIRST_GEN_BLADE_MK_III", "SECOND_GEN_BLADE"
                , "SECOND_GEN_BLADE_MK_II", "SECOND_GEN_BLADE_MK_III", "THIRD_GEN_BLADE", "THIRD_GEN_BLADE_MK_II"
                , "THIRD_GEN_BLADE_MK_III", "PERFECTED_BLADE").contains(blade))
        {
            sender.sendMessage("Invalid type");
            return;
        }

        if (target.getInventory().firstEmpty() == -1)
        {
            sender.sendMessage("Targets inv is full");
            return;
        }

        final Blades blades = Blades.valueOf(blade);

        final ItemBuilder builder = new ItemBuilder(Material.WOODEN_SWORD)
                .withName(blades.getFormattedName(blade))
                .withNBTString("blade", String.valueOf(true))
                .withNBTString("durability", String.valueOf(blades.getDurability()))
                .withNBTString("damage", String.valueOf(blades.getDamage()))
                .withNBTString("level", String.valueOf(blades.getLevel()))
                .withLore(blades.getDefaultLore());

        ItemStack item = builder.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null)
        {
            meta.setCustomModelData(blades.getModelData());
            item.setItemMeta(meta);
        }

        target.getInventory().addItem(item);
    }

}
