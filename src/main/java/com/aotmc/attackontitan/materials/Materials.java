package com.aotmc.attackontitan.materials;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum Materials
{
    TITAN_FRAGMENT(Material.LIGHT_GRAY_DYE, "Common"),
    TITAN_CRYSTAL(Material.BLUE_DYE, "Un-Common"),
    LARGE_TITAN_CRYSTAL(Material.NETHER_STAR, "Rare");

    public final Material material;
    public final String rarity;

    Materials(final Material material, final String rarity)
    {
        this.material = material;
        this.rarity = rarity;
    }

    public Material getMaterial()
    {
        return material;
    }

    public String getRarity()
    {
        return rarity;
    }

    public static String getFormattedName(final String item)
    {
        switch (item)
        {
            case "TITAN_FRAGMENT":
                return "&fTitan Fragment";
            case "TITAN_CRYSTAL":
                return "&bTitan Crystal";
            case "LARGE_TITAN_CRYSTAL":
                return "&eLarge Titan Crystal";
            default:
                return "Invalid Item";
        }
    }

    public List<String> getDefaultLore()
    {
        final List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(" &8• &7Rarity&8: &f" + rarity);
        lore.add(" &8• &7Left-Click for information!");
        lore.add("");

        return lore;
    }
}
