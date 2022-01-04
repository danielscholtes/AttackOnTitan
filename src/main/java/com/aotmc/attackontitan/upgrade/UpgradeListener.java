package com.aotmc.attackontitan.upgrade;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.GearType;
import com.aotmc.attackontitan.blades.BladeType;
import com.aotmc.attackontitan.general.util.Utils;
import com.aotmc.attackontitan.materials.MaterialData;
import com.aotmc.attackontitan.materials.PlayerMaterials;
import com.aotmc.attackontitan.odmgear.ODMType;
import com.codeitforyou.lib.api.item.ItemUtil;
import com.codeitforyou.lib.api.nbt.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class UpgradeListener implements Listener {

    private MaterialData materialData;

    public UpgradeListener(MaterialData materialData) {
        this.materialData = materialData;
    }

    @EventHandler
    public void onUpgradeGUI(InventoryClickEvent event) {
        if(Boolean.valueOf(ItemUtil.getNBTString(event.getCurrentItem(), "MenuItem"))) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equalsIgnoreCase(Utils.color("&cUpgrade"))) {
            return;
        }

        PlayerMaterials materials = materialData.getPlayerMaterials().get(player.getUniqueId());


        if (event.getRawSlot() == 20) {
            if ((!Boolean.valueOf(ItemUtil.getNBTString(event.getCursor(), "blade"))
                    && !Boolean.valueOf(ItemUtil.getNBTString(event.getCursor(), "odm")))
                    || Integer.parseInt(ItemUtil.getNBTString(event.getCursor(), "tier")) == 10) {
                Utils.createMenuBox(event.getInventory(), Material.RED_STAINED_GLASS_PANE, "&c", 10);
                event.getInventory().setItem(24, Utils.createMenuItem(Material.RED_STAINED_GLASS_PANE, "&c"));
                Utils.createMenuBox(event.getInventory(), Material.RED_STAINED_GLASS_PANE, "&c", 14);
                Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> player.updateInventory(), 1L);
                return;
            }

            boolean blade = Boolean.valueOf(ItemUtil.getNBTString(event.getCursor(), "blade"));

            ItemStack upgraded;
            GearType type;
            if (blade) {
                type = BladeType.find(Integer.parseInt(ItemUtil.getNBTString(event.getCursor(), "tier")) + 1).get();
                upgraded = Utils.createNewBlade((BladeType) type);
            } else {
                type = ODMType.find(Integer.parseInt(ItemUtil.getNBTString(event.getCursor(), "tier")) + 1).get();
                upgraded = Utils.createODMLeggings(type.getHierarchy(), 0);
            }

            NBT upgradeNBT = NBT.get(upgraded);
            upgradeNBT.setString("upgrade", String.valueOf(true));
            upgraded = upgradeNBT.apply(upgraded);

            Utils.createMenuBox(event.getInventory(), Material.GREEN_STAINED_GLASS_PANE, "&c", 10);

            List<String> requirements = new ArrayList<>();
            requirements.add(" &fRequirements:");

            if (materials.getSmallCrystals() >= type.getSmallCrystals()
                    && materials.getCrystals() >= type.getCrystals()
                    && materials.getLargeCrystals() >= type.getLargeCrystals()
                    && AttackOnTitan.getEconomy().getBalance(player) >= type.getPrice()) {
                requirements.add("   &2✔ &aSmall Crystals: " + materials.getSmallCrystals() + "/" + type.getSmallCrystals());
                requirements.add("   &2✔ &aCrystals: " + materials.getCrystals() + "/" + type.getCrystals());
                requirements.add("   &2✔ &aLarge Crystals: " + materials.getLargeCrystals() + "/" + type.getLargeCrystals());
                requirements.add("   &2✔ &aMoney: $" + (int) AttackOnTitan.getEconomy().getBalance(player) + "/$" + type.getPrice());
                requirements.add("");
                requirements.add(" &a&nYou can upgrade your blade&r  ");
                requirements.add("");

                removeRequirements(requirements, type);
                Utils.createMenuBox(event.getInventory(), Material.GREEN_STAINED_GLASS_PANE, "&c", requirements, 14);
            } else {
                String completedSmall = (materials.getSmallCrystals() >= type.getSmallCrystals()) ? "&2✔ &a" : "&4&l✗ &c";
                String completedRegular = (materials.getCrystals() >= type.getCrystals()) ? "&2✔ &a" : "&4&l✗ &c";
                String completedLarge = (materials.getLargeCrystals() >= type.getLargeCrystals()) ? "&2✔ &a" : "&4&l✗ &c";
                String completedMoney = (AttackOnTitan.getEconomy().getBalance(player) >= type.getPrice()) ? "&2✔ &a" : "&4&l✗ &c";
                requirements.add("   " + completedSmall + "Small Crystals: " + materials.getSmallCrystals() + "/" + type.getSmallCrystals());
                requirements.add("   " + completedRegular + "Crystals: " + materials.getCrystals() + "/" + type.getCrystals());
                requirements.add("   " + completedLarge + "Large Crystals: " + materials.getLargeCrystals() + "/" + type.getLargeCrystals());
                requirements.add("   " + completedMoney + "Money: $" + (int) AttackOnTitan.getEconomy().getBalance(player) + "/$" + type.getPrice());
                requirements.add("");
                requirements.add(" &c&nYou don't meet the requirements for upgrading&r  ");
                requirements.add("");

                removeRequirements(requirements, type);
                Utils.createMenuBox(event.getInventory(), Material.RED_STAINED_GLASS_PANE, "&c", requirements, 14);

                NBT menuNBT = NBT.get(upgraded);
                menuNBT.setString("MenuItem", String.valueOf(true));
                upgraded = menuNBT.apply(upgraded);
            }

            event.getInventory().setItem(24, upgraded);
            Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> player.updateInventory(), 1L);

        }

        if (event.getRawSlot() == 24) {
            if (!Boolean.valueOf(ItemUtil.getNBTString(event.getCurrentItem(), "upgrade"))) {
                return;
            }

            event.setCancelled(true);


            boolean blade = Boolean.valueOf(ItemUtil.getNBTString(event.getCurrentItem(), "blade"));

            ItemStack upgraded;
            GearType type;
            if (blade) {
                type = BladeType.find(Integer.parseInt(ItemUtil.getNBTString(event.getCurrentItem(), "tier"))).get();
                upgraded = Utils.createNewBlade((BladeType) type);
            } else {
                type = ODMType.find(Integer.parseInt(ItemUtil.getNBTString(event.getCurrentItem(), "tier"))).get();
                upgraded = Utils.createODMLeggings(type.getHierarchy(), 0);
            }
            Utils.createMenuBox(event.getInventory(), Material.RED_STAINED_GLASS_PANE, "&c", 10);
            Utils.createMenuBox(event.getInventory(), Material.RED_STAINED_GLASS_PANE, "&c", 14);
            event.getInventory().setItem(20, null);
            event.getInventory().setItem(24, Utils.createMenuItem(Material.RED_STAINED_GLASS_PANE, "&c"));
            event.setCursor(upgraded);
            Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
                player.updateInventory();
            }, 1L);

            materials.removeSmallCrystals(type.getSmallCrystals());
            materials.removeCrystals(type.getCrystals());
            materials.removeLargeCrystals(type.getLargeCrystals());
            AttackOnTitan.getEconomy().withdrawPlayer(player, type.getPrice());
        }
    }

    @EventHandler
    public void onCloseGUI(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!event.getView().getTitle().equalsIgnoreCase(Utils.color("&cUpgrade"))) {
            return;
        }

        if (event.getInventory().getItem(20) == null) {
            return;
        }
        player.getInventory().addItem(event.getInventory().getItem(20));
    }

    private void removeRequirements(List<String> requirements, GearType type) {
        if (type.getSmallCrystals() <= 0) {
            requirements.remove(1);
        }
        if (type.getCrystals() <= 0) {
            requirements.remove(2);
        }
        if (type.getLargeCrystals() <= 0) {
            requirements.remove(3);
        }
        if (type.getPrice() <= 0) {
            requirements.remove(4);
        }

    }

}
