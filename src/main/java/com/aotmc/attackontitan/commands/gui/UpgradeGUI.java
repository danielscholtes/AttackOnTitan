package com.aotmc.attackontitan.commands.gui;

import com.aotmc.attackontitan.AttackOnTitan;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.logging.Level;

public class UpgradeGUI
{

    private static final String   INVENTORY_NAME  = "          &8Blade Upgrades";
    private static final Material FILLER_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;

    @Command(permission = "aot.command.upgrade", aliases = {"upgrade"}, usage = "upgrade")
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {
        Inventory inventory = null;
        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate(INVENTORY_NAME), plugin);

            for (int i = 0; i < 27; i++)
            {
                if (i != 13)
                {
                    inventory.setItem(i, new ItemBuilder(FILLER_MATERIAL).withName(" ").getItem());
                }
            }

        }
        catch (InvalidInventoryException ex)
        {
            plugin.getLogger().log(Level.WARNING, "Failed to create inventory! ", ex);
        }

        if (inventory != null)
        {
            inventory.open(sender);
        }
    }

}
