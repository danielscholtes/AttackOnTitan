package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.blades.BladeType;
import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.item.ItemBuilder;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BladeCommand
{

    @Command(permission = "aot.command.give.blade", aliases = {"blade"}, usage = "<give/upgrade> [target] (blade)", requiredArgs = 1)
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {
        final String argument = args[0];
        final Player target = args.length == 3 ? Bukkit.getPlayerExact(args[1]) : null;
        final String blade = args.length == 3 ? args[2] : null;
        final List<String> blades = new ArrayList<>();

        for (BladeType bladeType : BladeType.values())
        {
            blades.add(bladeType.toString());
        }

        switch (argument)
        {

            case "give":
                if (target == null || !target.isOnline())
                {
                    sender.sendMessage(Utils.color("&cAdmin &8» '&c" + target + "&8' &7is not a valid target!"));
                    return;
                }
                if (!blades.contains(blade))
                {
                    sender.sendMessage(Utils.color("&cAdmin &8» '&f" + blade + "&8' &7is not a valid blade!"));
                    return;
                }

                final BladeType bladeType = BladeType.valueOf(blade.toUpperCase());
                target.getInventory().addItem(createNewBlade(bladeType));
                break;
            case "upgrade":
                final ItemStack playerItem = sender.getInventory().getItemInMainHand();
                final int blade_hierarchy = Integer.parseInt(ItemUtil.getNBTString(playerItem, "blade-hierarchy"));

                if (blade_hierarchy == 10)
                {
                    sender.sendMessage(Utils.color("&2Blade &8» &cThis blade can not be upgraded!"));
                    return;
                }

                final BladeType new_blade_type = BladeType.find(blade_hierarchy + 1).orElse(BladeType.valueOf(ItemUtil.getNBTString(sender.getInventory().getItemInMainHand(), "blade-type").toUpperCase()));

                sender.getInventory().setItemInMainHand(createNewBlade(new_blade_type));
                sender.sendMessage(Utils.color("&2Blade &8» &aYou have successfully upgraded your blade!"));
        }

    }

    private static ItemStack createNewBlade(final BladeType bladeType)
    {
        ItemStack item = new ItemBuilder(Material.WOODEN_SWORD)
                .withName(bladeType.getDisplayName())
                .withNBTString("blade", "true")
                .withNBTString("blade-type", bladeType.toString().toLowerCase())
                .withNBTString("blade-hierarchy", String.valueOf(bladeType.getHierarchy()))
                .withNBTString("blade-damage", String.valueOf(bladeType.getDamage()))
                .withNBTString("blade-durability", String.valueOf(bladeType.getDurability()))
                .withLore(bladeType.getLore())
                .withFlag(ItemFlag.HIDE_ATTRIBUTES).getItem();

        ItemMeta meta = item.getItemMeta();

        if (meta != null)
        {
            meta.setCustomModelData(1);

            item.setItemMeta(meta);
        }

        return item;
    }

}
