package com.aotmc.attackontitan.commands.listener;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aotmc.attackontitan.commands.gui.ConverterGUI;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.materials.Materials;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import com.codeitforyou.lib.api.item.ItemUtil;

public class ConverterListener implements Listener
{
    public static int inputItemSlot;

    @EventHandler
    public void onGUIMaterialConvert(InventoryClickEvent event)
    {
        final org.bukkit.inventory.Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null)
        {
            return;
        }

        final InventoryHolder clickedHolder = clickedInventory.getHolder();

        if (clickedHolder == null)
        {
            return;
        }

        inputItemSlot = event.getSlot();
        System.out.println(inputItemSlot);
        final ItemStack inputItem = event.getCurrentItem();
        final String inputNBT = ItemUtil.getNBTString(inputItem, "material");
        final Inventory inventory = ConverterGUI.inventory;

        if (!(clickedHolder instanceof CraftPlayer))
        {
            return;
        }

        if (inputItem == null || inputNBT == null || inventory == null)
        {
            return;
        }

        final int amount = inputItem.getAmount();

        switch (inputNBT)
        {
            case "TITAN_FRAGMENT":
                if (amount % 6 == 0)
                {
                    final int finishAmount = amount / 6;
                    inventory.setItem(15, new ItemBuilder(Materials.TITAN_CRYSTAL.getMaterial()).withName(finishAmount + "x " + Materials.getFormattedName("TITAN_CRYSTAL")).getItem());
                }
                else
                {
                    final int remainingAmount = amount % 6;
                    final int finishAmount = (amount - remainingAmount) / 6;
                    inventory.setItem(15, new ItemBuilder(Materials.TITAN_CRYSTAL.getMaterial()).withName(finishAmount + "x " + Materials.getFormattedName("TITAN_CRYSTAL")).getItem());
                }
                break;
            case "TITAN_CRYSTAL":
                final ItemStack LARGE_TITAN_CRYSTAL = new ItemStack(Materials.LARGE_TITAN_CRYSTAL.getMaterial());
                final ItemMeta LARGE_TITAN_CRYSTAL_META = LARGE_TITAN_CRYSTAL.getItemMeta();
                if (LARGE_TITAN_CRYSTAL_META != null)
                {
                    LARGE_TITAN_CRYSTAL_META.setDisplayName(Utils.color(Materials.getFormattedName("LARGE_TITAN_CRYSTAL")));
                }
                LARGE_TITAN_CRYSTAL.setItemMeta(LARGE_TITAN_CRYSTAL_META);

                if (amount % 14 == 0)
                {
                    final int finishAmount = amount / 14;
                    LARGE_TITAN_CRYSTAL.setAmount(finishAmount);
                    inventory.setItem(15, LARGE_TITAN_CRYSTAL);
                }
                else
                {
                    final int remainingAmount = amount % 14;
                    final int finishAmount = (amount - remainingAmount) / 14;
                    LARGE_TITAN_CRYSTAL.setAmount(finishAmount);
                    inventory.setItem(15, LARGE_TITAN_CRYSTAL);
                }
                break;
            case "LARGE_TITAN_CRYSTAL":
                inventory.setItem(15, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName("&cUnavailable").withLore("", " &cThis item can not be", " &cconverted as it is the rarest").getItem());
                break;
        }

        inventory.setItem(11, inputItem);
    }

}
