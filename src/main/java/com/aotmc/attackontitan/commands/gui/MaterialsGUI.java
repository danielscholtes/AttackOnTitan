package com.aotmc.attackontitan.commands.gui;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.materials.Materials;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.logging.Level;

public class MaterialsGUI
{

    private static final String   INVENTORY_NAME  = "             &8Materials";
    private static final Material FILLER_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;

    @Command(permission = "aot.command.materials", aliases = {"materials"}, usage = "materials")
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {
        Inventory inventory = null;
        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate(INVENTORY_NAME), plugin);

            for (int i = 0; i < 27; i++)
            {
                if (i != 11 && i != 13 && i != 15)
                {
                    inventory.setItem(i, new ItemBuilder(FILLER_MATERIAL).withName(" ").getItem());
                }

                if (i == 11)
                {
                    inventory.setItem(i, new ItemBuilder(Materials.TITAN_FRAGMENT.getMaterial()).withName(Materials.getFormattedName("TITAN_FRAGMENT")).getItem(), ((player, action) ->
                    {
                        player.closeInventory();
                        for (final String fragmentLore : Materials.getTitanFragmentLore())
                        {
                            player.sendMessage(StringUtil.translate(fragmentLore));
                        }
                    }));
                }
                if (i == 13)
                {
                    inventory.setItem(i, new ItemBuilder(Materials.TITAN_CRYSTAL.getMaterial()).withName(Materials.getFormattedName("TITAN_CRYSTAL")).getItem(), ((player, action) ->
                    {
                        player.closeInventory();
                        for (final String crystalLore : Materials.getTitanCrystalLore())
                        {
                            player.sendMessage(StringUtil.translate(crystalLore));
                        }
                    }));
                }
                if (i == 15)
                {
                    inventory.setItem(i, new ItemBuilder(Materials.LARGE_TITAN_CRYSTAL.getMaterial()).withName(Materials.getFormattedName("LARGE_TITAN_CRYSTAL")).getItem(), ((player, action) ->
                    {
                        player.closeInventory();
                        for (final String largeCrystalLore : Materials.getLargeTitanCrystalLore())
                        {
                            player.sendMessage(StringUtil.translate(largeCrystalLore));
                        }
                    }));
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
