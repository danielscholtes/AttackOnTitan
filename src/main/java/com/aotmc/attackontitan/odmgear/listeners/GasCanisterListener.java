package com.aotmc.attackontitan.odmgear.listeners;

import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class GasCanisterListener implements Listener {

    @EventHandler
    public void onGasCanisterUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() == Action.PHYSICAL) {
            return;
        }

        if (player.getInventory().getLeggings() == null || !Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getLeggings(), "odm"))) {
            return;
        }

        if (event.getItem() == null || !Boolean.valueOf(ItemUtil.getNBTString(event.getItem(), "gascanister"))) {
            return;
        }

        int gas = Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getLeggings(), "gas"));
        if (gas == 0) {
            return;
        }
        int tier = Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getLeggings(), "tier"));

        gas -= Integer.parseInt(ItemUtil.getNBTString(event.getItem(), "gas"));
        gas = Math.max(gas, 0);

        player.getEquipment().setLeggings(Utils.createODMHoe(tier, gas));
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }

}
