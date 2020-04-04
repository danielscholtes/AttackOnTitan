package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.materials.Materials;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GiveMaterialCommand
{

    @Command(permission = "aot.command.give.material", aliases = {"givematerial"}, usage = "<target> (material) [amount]", requiredArgs = 3)
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {
        final Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null || !target.isOnline())
        {
            sender.sendMessage("Invalid target");
            return;
        }

        final Materials materials = Materials.valueOf(args[1].toUpperCase());
        final int amount = Integer.parseInt(args[2]);

        Material material;
        String display_name;

        switch(materials)
        {
            case TITAN_FRAGMENT:
                material = Materials.TITAN_FRAGMENT.getMaterial();
                display_name = Materials.getFormattedName("TITAN_FRAGMENT");

                break;
            case TITAN_CRYSTAL:
                material = Materials.TITAN_CRYSTAL.getMaterial();
                display_name = Materials.getFormattedName("TITAN_CRYSTAL");

                break;
            case LARGE_TITAN_CRYSTAL:
                material = Materials.LARGE_TITAN_CRYSTAL.getMaterial();
                display_name = Materials.getFormattedName("LARGE_TITAN_CRYSTAL");

                break;
            default:
                sender.sendMessage("Invalid material!");
                return;
        }

        if (target.getInventory().firstEmpty() == -1)
        {
            sender.sendMessage("Target has full inv");
            return;
        }

        final ItemBuilder builder = new ItemBuilder(material)
                .withName(display_name)
                .withNBTString("material", String.valueOf(materials));

        for (int i = 0; i < amount; i++)
        {
            target.getInventory().addItem(builder.getItem());
        }
    }

}
