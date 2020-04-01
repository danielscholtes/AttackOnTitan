package com.aotmc.attackontitan.commands.inventory;

import com.aotmc.attackontitan.AttackOnTitan;
import com.codeitforyou.lib.api.exception.InvalidInventoryException;
import com.codeitforyou.lib.api.general.StringUtil;
import com.codeitforyou.lib.api.inventory.Inventory;
import com.codeitforyou.lib.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

public class UpgradeBladeInventory
{

    public static Inventory getInventory()
    {
        Inventory inventory = null;
        try
        {
            inventory = new Inventory(InventoryType.CHEST, StringUtil.translate("            &8Upgrades"), AttackOnTitan.getInstance());
            for (int i = 0; i < 27; i++)
            {
                if (i == 0 || i == 9 || i == 18)
                {
                    inventory.setItem(i, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withName("&cCancel").withLore("", " &8• &7Click to cancel!", "").withNBTString("inventory", "upgrades").getItem(), ((player, action) ->
                    {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate("&8[&cUpgrades&8] &7You have cancelled the upgrade!"));
                            player.closeInventory();
                        }
                    }));
                }
                else if (i == 8 || i == 17 || i == 26)
                {
                    inventory.setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withName("&aUpgrade").withLore("", " &8• &7Click to upgrade!", "").withNBTString("inventory", "upgrades").getItem(), ((player, action) ->
                    {
                        if (action == ClickType.LEFT)
                        {
                            player.sendMessage(StringUtil.translate("&8[&aUpgrades&8] &7You have successfully upgraded your blade!"));
                            player.closeInventory();
                        }
                    }));
                }
                else if (i != 11 && i != 13 && i != 15)
                {
                    inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).withName(" ").withNBTString("inventory", "upgrades").getItem());
                }
            }
        }
        catch (InvalidInventoryException ex)
        {
            ex.printStackTrace();
        }

        return inventory;
    }

}
