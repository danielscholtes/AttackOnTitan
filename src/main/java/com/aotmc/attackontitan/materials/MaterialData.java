package com.aotmc.attackontitan.materials;

import com.aotmc.attackontitan.AttackOnTitan;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MaterialData {

    private AttackOnTitan plugin;
    private final File DATA_FOLDER;

    public MaterialData(AttackOnTitan plugin) {
        this.plugin = plugin;
        DATA_FOLDER = new File(plugin.getDataFolder().getPath() + File.separator + "data");
        if (!DATA_FOLDER.exists()) {
            DATA_FOLDER.mkdirs();
        }
    }

    private Map<UUID, PlayerMaterials> playerMaterials = new HashMap<>();

    public void loadData(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            File file = new File(DATA_FOLDER.getPath() + File.separator + uuid.toString() + ".yml");
            if (!file.exists()) {
                playerMaterials.put(uuid, new PlayerMaterials(0, 0, 0));
                return;
            }
            YamlConfiguration fileData = YamlConfiguration.loadConfiguration(file);
            int smallCrystals = fileData.getInt("small_crystals");
            int crystals = fileData.getInt("crystals");
            int largeCrystals = fileData.getInt("large_crystals");
            playerMaterials.put(uuid, new PlayerMaterials(smallCrystals, crystals, largeCrystals));
        });
    }

    public void saveData(UUID uuid, boolean removeFromMap) {
        File file = new File(DATA_FOLDER.getPath() + File.separator + uuid.toString() + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration fileData = YamlConfiguration.loadConfiguration(file);
        fileData.set("small_crystals", playerMaterials.get(uuid).getSmallCrystals());
        fileData.set("crystals", playerMaterials.get(uuid).getCrystals());
        fileData.set("large_crystals", playerMaterials.get(uuid).getLargeCrystals());
        try {
            fileData.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (removeFromMap) {
            playerMaterials.remove(uuid);
        }
    }

    public void saveAllData() {
        for (UUID uuid : playerMaterials.keySet()) {
            saveData(uuid, false);
        }
    }

    public Map<UUID, PlayerMaterials> getPlayerMaterials() {
        return playerMaterials;
    }
}
