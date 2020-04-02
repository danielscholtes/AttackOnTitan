package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class UpgradeGuiCommand
{

    public static Inventory inventory;

    @Command(permission = "aot.command.upgrade", aliases = {"upgrade"}, usage = "upgrade")
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {

        inventory = null;
        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate("          &8Blade Upgrades"), plugin);
            for (int i = 0; i < 27; i++)
            {
                if (i == 13)
                {
                    inventory.setItem(i, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).withNBTString("upgrade-inventory", "upgrade-slot").getItem(), ((player, action) ->
                    {


                    }));
                }
                else
                {
                    inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).withNBTString("upgrade-inventory", "filler").getItem());
                }
            }
        }
        catch (InvalidInventoryException ex)
        {
            ex.printStackTrace();
        }

        if (inventory != null)
        {
            inventory.open(sender);
        }

    }

}
