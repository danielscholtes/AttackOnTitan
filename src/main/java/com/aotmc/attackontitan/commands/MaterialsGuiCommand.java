package com.aotmc.attackontitan.commands;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.materials.Materials;
import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

public class MaterialsGuiCommand
{

    @Command(permission = "aot.command.materials", aliases = {"materials"}, usage = "materials")
    public static void execute(final Player sender, final AttackOnTitan plugin, final String[] args)
    {

        Inventory inventory = null;
        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate("             &8Materials"), plugin);
            for (int i = 0; i < 27; i++)
            {
                if (i == 11)
                {
                    inventory.setItem(i, new ItemBuilder(Materials.TITAN_FRAGMENT.getMaterial()).withName(Materials.getFormattedName("TITAN_FRAGMENT")).withLore(Materials.TITAN_FRAGMENT.getDefaultLore()).getItem(), ((player, action) -> {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate(""));
                            player.sendMessage(StringUtil.translate(" " + Materials.getFormattedName("TITAN_FRAGMENT")));
                            player.sendMessage(StringUtil.translate(" "));
                            player.sendMessage(StringUtil.translate(" &8• &7This material is most common and"));
                            player.sendMessage(StringUtil.translate(" &8• &7can be obtained by killing Small"));
                            player.sendMessage(StringUtil.translate(" &8• &7and Medium Titans&8! &7Can be converted"));
                            player.sendMessage(StringUtil.translate(" &8• &7using the material converter&8!"));
                            player.sendMessage(StringUtil.translate(""));
                            player.closeInventory();
                        }
                    }));
                }
                else if (i == 13)
                {
                    inventory.setItem(i, new ItemBuilder(Materials.TITAN_CRYSTAL.getMaterial()).withName(Materials.getFormattedName("TITAN_CRYSTAL")).withLore(Materials.TITAN_CRYSTAL.getDefaultLore()).getItem(), ((player, action) ->
                    {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate(""));
                            player.sendMessage(StringUtil.translate(" " + Materials.getFormattedName("TITAN_CRYSTAL")));
                            player.sendMessage(StringUtil.translate(" "));
                            player.sendMessage(StringUtil.translate(" &8• &7This material is not as common and"));
                            player.sendMessage(StringUtil.translate(" &8• &7can be obtained by killing Medium"));
                            player.sendMessage(StringUtil.translate(" &8• &7and Large Titans&8! &7Can be converted"));
                            player.sendMessage(StringUtil.translate(" &8• &7using the material converter&8!"));
                            player.sendMessage(StringUtil.translate(""));
                            player.closeInventory();
                        }
                    }));
                }
                else if (i == 15)
                {
                    inventory.setItem(i, new ItemBuilder(Materials.LARGE_TITAN_CRYSTAL.getMaterial()).withName(Materials.getFormattedName("LARGE_TITAN_CRYSTAL")).withLore(Materials.LARGE_TITAN_CRYSTAL.getDefaultLore()).getItem(), ((player, action) ->
                    {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate(""));
                            player.sendMessage(StringUtil.translate(" " + Materials.getFormattedName("LARGE_TITAN_CRYSTAL")));
                            player.sendMessage(StringUtil.translate(" "));
                            player.sendMessage(StringUtil.translate(" &8• &7This material is the rarest and"));
                            player.sendMessage(StringUtil.translate(" &8• &7can be obtained by killing Large"));
                            player.sendMessage(StringUtil.translate(" &8• &7Titans&8! &7Can not be converted"));
                            player.sendMessage(StringUtil.translate(" &8• &7as it is the highest level&8!"));
                            player.sendMessage(StringUtil.translate(""));
                            player.closeInventory();
                        }
                    }));
                }
                else
                {
                    inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).withName(" ").getItem());
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
