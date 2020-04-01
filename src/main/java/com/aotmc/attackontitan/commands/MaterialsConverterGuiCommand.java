package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

public class MaterialsConverterGuiCommand
{

    public static Inventory inventory;
    public static String uuid;

    @Command(permission = "aot.command.converter", aliases = {"converter"}, usage = "converter")
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {

        inventory = null;
        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate("             &8Converter"), plugin);
            for (int i = 0; i < 27; i++)
            {

                if (i == 0 || i == 9 || i == 18)
                {
                    inventory.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName("&cCancel").withLore("", " &8• &7Click to cancel!", "").withNBTString("inventory", "converter").getItem(), ((player, action) ->
                    {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate("&8[&cConverter&8] &7You have cancelled the conversion!"));
                            player.closeInventory();
                        }
                    }));
                }
                else if (i == 8 || i == 17 || i == 26)
                {
                    inventory.setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withName("&aConvert").withLore("", " &8• &7Click to convert!", "").withNBTString("inventory", "converter").getItem(), ((player, action) ->
                    {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate("&8[&aConverter&8] &7You have successfully converted your materials!"));
                            player.closeInventory();
                        }
                    }));
                }
                else if (i == 4 || i == 13 || i == 22)
                {
                    inventory.setItem(i, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).withName(" ").withLore(" &7<- Input Material", "", " &7Output Material ->", "").withNBTString("inventory", "converter").getItem());
                }
                else
                {
                    if (i != 11 && i != 15)
                    {
                        inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).withName(" ").withNBTString("inventory", "converter").getItem());
                    }
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
            uuid = inventory.getUuid().toString();
        }

    }

}
