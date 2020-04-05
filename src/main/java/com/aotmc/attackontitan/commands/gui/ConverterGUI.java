package com.aotmc.attackontitan.commands.gui;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.commands.listener.ConverterListener;
import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import com.codeitforyou.lib.api.item.ItemUtil;

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

                if (i == 0 || i == 9 || i == 18)
                {
                    inventory.setItem(i, new ItemBuilder(CLOSE_MATERIAL).withName("&cClose").getItem(), ((player, action) ->
                    {
                        player.closeInventory();
                        player.sendMessage(StringUtil.translate("&2Converter &8» &cYou have closed the resource converter."));
                    }));
                }

                if (i == 8 || i == 17 || i == 26)
                {
                    inventory.setItem(i, new ItemBuilder(CONVERT_MATERIAL).withName("&aConvert").getItem(), ((player, action) ->
                    {
                        final ItemStack input = inventory.get().getItem(11);
                        final String inputNBT = ItemUtil.getNBTString(input, "material");
                        ItemStack output = inventory.get().getItem(15);

                        if (output != null)
                        {
                            output = output.clone();
                        }

                        if (input != null && output != null)
                        {
                            final int inputAmount = input.getAmount();

                            switch (inputNBT)
                            {
                                case "TITAN_FRAGMENT":
                                    if (inputAmount % 6 == 0)
                                    {
                                        player.getInventory().remove(input);
                                        for (int slot = 0; slot < output.getAmount(); slot++)
                                        {
                                            player.getInventory().addItem(new ItemBuilder(output.getType()).withName(output.getItemMeta().getDisplayName()).withNBTString("material", "TITAN_CRYSTAL").getItem());
                                        }
                                        player.sendMessage(Utils.color("&2Converter &8» &aYou have successfully converted your materials."));
                                        player.closeInventory();
                                        break;
                                    }

                                    final int finishAmount = (inputAmount - (inputAmount % 6)) / 6;
                                    player.getInventory().getItem(ConverterListener.inputItemSlot).setAmount(finishAmount);
                                    for (int slot = 0; slot < output.getAmount(); slot++)
                                    {
                                        player.getInventory().addItem(new ItemBuilder(output.getType()).withName(output.getItemMeta().getDisplayName()).withNBTString("material", "TITAN_CRYSTAL").getItem());
                                    }
                                    player.sendMessage(Utils.color("&2Converter &8» &a You have successfully converted your materials."));
                                    player.closeInventory();
                                    break;
                                case "TITAN_CRYSTAL":
                                    if (inputAmount % 14 == 0)
                                    {
                                        player.getInventory().remove(input);
                                        for (int slot = 0; slot < output.getAmount(); slot++)
                                        {
                                            player.getInventory().addItem(new ItemBuilder(output.getType()).withName(output.getItemMeta().getDisplayName()).withNBTString("material", "LARGE_TITAN_CRYSTAL").getItem());
                                        }
                                        player.sendMessage(Utils.color("&2Converter &8» &aYou have successfully converted your materials."));
                                        player.closeInventory();
                                        break;
                                    }

                                    final int finishAmount2 = (inputAmount - (inputAmount % 14)) / 14;
                                    player.getInventory().getItem(ConverterListener.inputItemSlot).setAmount(finishAmount2);
                                    for (int slot = 0; slot < output.getAmount(); slot++)
                                    {
                                        player.getInventory().addItem(new ItemBuilder(output.getType()).withName(output.getItemMeta().getDisplayName()).withNBTString("material", "LARGE_TITAN_CRYSTAL").getItem());
                                    }
                                    player.sendMessage(Utils.color("&2Converter &8» &aYou have successfully converted your materials."));
                                    player.closeInventory();
                                    break;
                                case "LARGE_TITAN_CRYSTAL":
                                    player.sendMessage(Utils.color("&2Converter &8» &cYou can not convert this resource!"));
                                    player.closeInventory();
                                    break;
                            }
                        }
                    }));
                }
            }

            sender.sendMessage(Utils.color("&2Converter &8» &7You have opened the resource converter."));
            inventory.open(sender);
        }
        catch (InvalidInventoryException ex)
        {
            plugin.getLogger().log(Level.WARNING, "Failed to create inventory! ", ex);
        }

    }

}
