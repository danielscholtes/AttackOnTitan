package com.aotmc.attackontitan.commands.listener;

import com.aotmc.attackontitan.commands.gui.ConverterGUI;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ConverterListener implements Listener
{

    @EventHandler
    public void onGUIMaterialConvert(InventoryClickEvent event)
    {
        final InventoryHolder holder = event.getInventory().getHolder();
        final ItemStack inputItem = event.getCursor();
        final String inputNBT = ItemUtil.getNBTString(inputItem, "material");
        final Inventory inventory = ConverterGUI.inventory;

        if (!(holder instanceof ConverterGUI))
        {
            return;
        }

        if (inputItem == null || inputNBT == null || inventory == null)
        {
            return;
        }

        System.out.println(inputItem);
        inventory.setItem(11, inputItem);
    }

}
