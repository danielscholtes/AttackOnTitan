package com.aotmc.attackontitan.commands.gui.listener;

import com.aotmc.attackontitan.commands.UpgradeGuiCommand;
import com.aotmc.attackontitan.commands.inventory.UpgradeBladeInventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryClickListener implements Listener
{

    public ItemStack upgradeBlade;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        final String firstItemNBT = ItemUtil.getNBTString(event.getInventory().getItem(0), "inventory");

        if (firstItemNBT == null)
        {
            System.out.println("null nbt data");
            return;
        }

        switch (firstItemNBT)
        {
            case "upgrade-blade":
                System.out.println("upgrade blade inventory");

                final ItemStack item = event.getCurrentItem();
                final String nbtValue = ItemUtil.getNBTString(item, "blade");
                final String nbtLevel = ItemUtil.getNBTString(item, "level");

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
                        UpgradeGuiCommand.inventory.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName(" ").withLore(" &cThis blade has been ", " &cupgraded to the max!", "").withNBTString("type", "close").getItem());
                    }
                }
                else
                {
                    for (int i : slots)
                    {
                        UpgradeGuiCommand.inventory.setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withName(" ").withLore(" &aClick to upgrade ", "  &athis blade!", "").withNBTString("type", "upgrade").getItem());
                    }
                }

                if (event.getClickedInventory().getItem(13) != null || event.getClick() == ClickType.LEFT)
                {
                    System.out.println("upgradeable item isn't null");
                    if (!(event.getWhoClicked() instanceof Player))
                    {
                        return;
                    }
                    final Player player = (Player) event.getWhoClicked();

                    if (12 == event.getSlot())
                    {
                        if (event.getClickedInventory().getItem(12).isSimilar(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withNBTString("type", "upgrade").getItem()))
                        {
                            upgradeBlade = event.getClickedInventory().getItem(13);
                            player.closeInventory();
                            UpgradeBladeInventory.getInventory().open(player);
                        }
                        else if (event.getClickedInventory().getItem(12).isSimilar(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withNBTString("type", "close").getItem()))
                        {
                            player.sendMessage("You have canceled the upgrade!");
                            player.closeInventory();
                        }
                    }
                }
                break;
            case "converter":

        }
    }

}
