package com.aotmc.attackontitan.commands.gui;

import com.aotmc.attackontitan.AttackOnTitan;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class ConverterGUI
{

    private static final String INVENTORY_NAME = "             &8Converter";

    private static final Material FILLER_MATERIAL    = Material.GRAY_STAINED_GLASS_PANE;
    private static final Material CLOSE_MATERIAL     = Material.RED_STAINED_GLASS_PANE;
    private static final Material CONVERT_MATERIAL   = Material.LIME_STAINED_GLASS_PANE;
    private static final Material SEPARATOR_MATERIAL = Material.BLUE_STAINED_GLASS_PANE;

    public static Inventory inventory;

    @Command(permission = "aot.command.converter", aliases = {"converter"}, usage = "converter")
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {

        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate(INVENTORY_NAME), plugin);

            for (int i = 0; i < 27; i++)
            {
                if (i != 0 && i != 9 && i != 18 && i != 8 && i != 17 && i != 26 && i != 4 && i != 13 && i != 22 && i != 11 && i != 15)
                {
                    inventory.setItem(i, new ItemBuilder(FILLER_MATERIAL).withName(" ").getItem());
                }

                if (i == 4 || i == 13 || i == 22)
                {
                    inventory.setItem(i, new ItemBuilder(SEPARATOR_MATERIAL).withName(" ").getItem());
                }

                if (i == 0 || i == 9 || i == 17)
                {
                    inventory.setItem(i, new ItemBuilder(CLOSE_MATERIAL).withName("&cClose").getItem(), ((player, action) ->
                    {
                        player.closeInventory();
                        player.sendMessage(StringUtil.translate("&c&lConverter &8» &7You have closed the converter!"));
                    }));
                }

                if (i == 8 || i == 17 || i == 26)
                {
                    inventory.setItem(i, new ItemBuilder(CONVERT_MATERIAL).withName("&aConvert").getItem(), ((player, action) ->
                    {
                        final ItemStack input = inventory.get().getItem(11);

                        if (input != null)
                        {
                            final String inputNBT = ItemUtil.getNBTString(input, "material");

                            if (inputNBT != null)
                            {
                                switch (inputNBT)
                                {
                                    case "TITAN_FRAGMENT":
                                        player.sendMessage("Titan Fragment");
                                        break;
                                    case "TITAN_CRYSTAL":
                                        player.sendMessage("Titan Crystal");
                                        break;
                                    case "LARGE_TITAN_CRYSTAL":
                                        player.sendMessage("Large Titan Crystal");
                                        break;
                                }
                                player.closeInventory();
                            }
                        }
                    }));
                }
            }

            inventory.open(sender);
        }
        catch (InvalidInventoryException ex)
        {
            plugin.getLogger().log(Level.WARNING, "Failed to create inventory! ", ex);
        }

    }

}
