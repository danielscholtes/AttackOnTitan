package com.aotmc.attackontitan.blades;

import java.util.ArrayList;
import java.util.List;

public enum Blades
{

    FIRST_GEN_BLADE(1, 10, 10, "&7⭐"),
    FIRST_GEN_BLADE_MK_II(1, 10, 10, "&7⭐⭐"),
    FIRST_GEN_BLADE_MK_III(1, 10, 10, "&7⭐⭐⭐"),
    SECOND_GEN_BLADE_MK_II(1, 10, 10, "&7⭐⭐⭐⭐"),
    SECOND_GEN_BLADE_MK_III(1, 10, 10, "&7⭐⭐⭐⭐⭐"),
    SECOND_GEN_BLADE(1, 10, 10, "&6⭐&7⭐⭐⭐⭐"),
    THIRD_GEN_BLADE(1, 10, 10, "&6⭐⭐&7⭐⭐⭐"),
    THIRD_GEN_BLADE_MK_II(1, 10, 10, "&6⭐⭐⭐&7⭐⭐"),
    THIRD_GEN_BLADE_MK_III(1, 10, 10, "&6⭐⭐⭐⭐&7⭐"),
    PRESTIGED_BLADE(1, 10, 10, "&6⭐⭐⭐⭐⭐");

    public final int    modelData;
    public final double damage;
    public final double durability;
    public final String rating;

    Blades(final int modelData, final double damage, final double durability, final String rating)
    {
        this.modelData = modelData;
        this.damage = damage;
        this.durability = durability;
        this.rating = rating;
    }

    public int getModelData()
    {
        return modelData;
    }

    public double getDamage()
    {
        return damage;
    }

    public double getDurability()
    {
        return durability;
    }

    public String getFormattedName(final String blade)
    {
        switch (blade)
        {
            case "FIRST_GEN_BLADE":
                return "&fFirst Generation Blade";
            case "FIRST_GEN_BLADE_MK_II":
                return "&fFirst Generation Blade Mk. II";
            case "FIRST_GEN_BLADE_MK_III":
                return "&fFirst Generation Blade Mk. III";
            case "SECOND_GEN_BLADE":
                return "&bSecond Generation Blade";
            case "SECOND_GEN_BLADE_MK_II":
                return "&bSecond Generation Blade Mk. II";
            case "SECOND_GEN_BLADE_MK_III":
                return "&bSecond Generation Blade Mk. III";
            case "THIRD_GEN_BLADE":
                return "&dThird Generation Blade";
            case "THIRD_GEN_BLADE_MK_II":
                return "&dThird Generation Blade Mk. II";
            case "THIRD_GEN_BLADE_MK_III":
                return "&dThird Generation Blade Mk. III";
            case "PRESTIGED_BLADE":
                return "&5Perfected Blade";
            default:
                return "Invalid Blade";
        }
    }

    public List<String> getDefaultLore(final String blade)
    {
        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(" &8• &7Damage&8: &f" + damage);
        lore.add(" &8• &7Durability&8: &f" + durability);
        lore.add(" &8• &7Rating&8: " + rating);

        return lore;
    }
}
