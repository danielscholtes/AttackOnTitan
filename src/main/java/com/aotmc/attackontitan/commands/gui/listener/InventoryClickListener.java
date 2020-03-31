package com.aotmc.attackontitan.commands.gui.listener;

import com.aotmc.attackontitan.commands.UpgradeGuiCommand;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryClickListener implements Listener
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {

        final ItemStack item = event.getCurrentItem();
        final String nbtValue = ItemUtil.getNBTString(item, "blade");
        final String nbtLevel = ItemUtil.getNBTString(item, "level");

        if (uuid == null)
        {
            return;
        }

        if (item == null)
        {
            return;
        }

        if (!Boolean.valueOf(nbtValue))
        {
            return;
        }

        if (nbtLevel == null)
        {
            return;
        }

        final int level = Integer.parseInt(nbtLevel);
        final List<Integer> slots = Arrays.asList(3, 4, 5, 12, 14, 21, 22, 23);

        UpgradeGuiCommand.inventory.setItem(13, item);
        if (level == 10)
        {
            for (int i : slots)
            {
                UpgradeGuiCommand.inventory.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName(" ").withLore(" &cThis blade has been ", " &cupgraded to the max!", "").getItem());
            }
        }
        else
        {
            for (int i : slots)
            {
                UpgradeGuiCommand.inventory.setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withName(" ").withLore(" &aClick to upgrade ", "  &athis blade!", "").getItem());
            }
        }
    }
}
