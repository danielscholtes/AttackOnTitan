package com.aotmc.attackontitan.upgrade;

import com.aotmc.attackontitan.general.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class UpgradeCommand implements CommandExecutor {

    private final int SIZE = 45;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        Inventory upgradeGUI = Bukkit.createInventory(null, SIZE, Utils.color("&cUpgrade"));
        for (int i = 0; i < SIZE; i++) {
            upgradeGUI.setItem(i, Utils.createMenuItem(Material.GRAY_STAINED_GLASS_PANE, "&c"));
        }

        upgradeGUI.setItem(20, null);
        upgradeGUI.setItem(24, Utils.createMenuItem(Material.RED_STAINED_GLASS_PANE, "&c"));
        Utils.createMenuBox(upgradeGUI, Material.RED_STAINED_GLASS_PANE, "&c", 10);
        Utils.createMenuBox(upgradeGUI, Material.RED_STAINED_GLASS_PANE, "&c", 14);
        player.openInventory(upgradeGUI);

        return true;
    }

}
