package com.aotmc.attackontitan.commands.listener;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
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
                final ItemStack titanCrystal = new ItemStack(Materials.TITAN_CRYSTAL.getMaterial());
                final ItemMeta titanCrystalMeta = titanCrystal.getItemMeta();
                if (titanCrystalMeta != null)
                {
                    titanCrystalMeta.setDisplayName(Utils.color(Materials.getFormattedName("TITAN_CRYSTAL")));
                }
                titanCrystal.setItemMeta(titanCrystalMeta);

                if (amount % 6 == 0)
                {
                    final int finishAmount = amount / 6;
                    titanCrystal.setAmount(finishAmount);
                    inventory.setItem(15, titanCrystal);
                }
                else
                {
                    final int remainingAmount = amount % 6;
                    final int finishAmount = (amount - remainingAmount) / 6;
                    titanCrystal.setAmount(finishAmount);
                    inventory.setItem(15, titanCrystal);
                }
                break;
            case "TITAN_CRYSTAL":
                final ItemStack largeTitanCrystal = new ItemStack(Materials.LARGE_TITAN_CRYSTAL.getMaterial());
                final ItemMeta largeTitanCrystalMeta = largeTitanCrystal.getItemMeta();
                if (largeTitanCrystalMeta != null)
                {
                    largeTitanCrystalMeta.setDisplayName(Utils.color(Materials.getFormattedName("LARGE_TITAN_CRYSTAL")));
                }
                largeTitanCrystal.setItemMeta(largeTitanCrystalMeta);

                if (amount % 14 == 0)
                {
                    final int finishAmount = amount / 14;
                    largeTitanCrystal.setAmount(finishAmount);
                    inventory.setItem(15, largeTitanCrystal);
                }
                else
                {
                    final int remainingAmount = amount % 14;
                    final int finishAmount = (amount - remainingAmount) / 14;
                    largeTitanCrystal.setAmount(finishAmount);
                    inventory.setItem(15, largeTitanCrystal);
                }
                break;
            case "LARGE_TITAN_CRYSTAL":
                inventory.setItem(15, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName("&cUnavailable").withLore("", " &cThis item can not be", " &cconverted as it is the rarest").getItem());
                break;
        }

        inventory.setItem(11, inputItem);
    }

}
