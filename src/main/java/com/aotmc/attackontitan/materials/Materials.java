package com.aotmc.attackontitan.materials;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum Materials
{
    TITAN_FRAGMENT(Material.LIGHT_GRAY_DYE, "&6⭐&7⭐⭐"),
    TITAN_CRYSTAL(Material.BLUE_DYE, "&6⭐⭐&7⭐"),
    LARGE_TITAN_CRYSTAL(Material.NETHER_STAR, "&6⭐⭐⭐");

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

    public static List<String> getTitanFragmentLore()
    {
        final List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(" &f&nTitan Fragment");
        lore.add("");
        lore.add(" &8• &7A titan fragment is the most common");
        lore.add(" &8• &7of the various titan drops, and");
        lore.add(" &8• &7can be used to craft common materials");
        lore.add(" &8• &7which can later be used to craft blades!");
        lore.add("");

        return lore;
    }

    public static List<String> getTitanCrystalLore()
    {
        final List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(" &b&nTitan Crystal");
        lore.add("");
        lore.add(" &8• &7The second most common titan drop");
        lore.add(" &8• &7which can be used for crafting blades");
        lore.add(" &8• &7or combined into rarer materials!");
        lore.add("");

        return lore;
    }

    public static List<String> getLargeTitanCrystalLore()
    {
        final List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(" &e&nLarge Titan Crystal");
        lore.add("");
        lore.add(" &8• &7Amongst the rarest titan drops");
        lore.add(" &8• &7primarily used for crafting and");
        lore.add(" &8• &7upgrading higher tiered blades!");
        lore.add("");

        return lore;
    }

}
