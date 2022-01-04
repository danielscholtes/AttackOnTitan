package com.aotmc.attackontitan.materials;

import com.aotmc.attackontitan.AttackOnTitan;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private MaterialData materialData;

    public PlayerListener(MaterialData materialData) {
        this.materialData = materialData;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(AttackOnTitan.getInstance(), () -> {
            materialData.loadData(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        materialData.saveData(event.getPlayer().getUniqueId(), true);
    }
}
