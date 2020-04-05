package com.aotmc.attackontitan.blades;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum BladeType implements BladeConstructor
{

    FIRST_GEN_BLADE(1, 2.3, 3, "&7⭐", "&fFirst Generation Blade"),
    FIRST_GEN_BLADE_MK_II(2, 3.7, 6, "&7⭐⭐", "&fFirst Generation Blade Mk. II"),
    FIRST_GEN_BLADE_MK_III(3, 5.3, 9, "&7⭐⭐⭐", "&fFirst Generation Blade Mk. III"),
    SECOND_GEN_BLADE(4, 7.1, 12, "&7⭐⭐⭐⭐", "&bSecond Generation Blade"),
    SECOND_GEN_BLADE_MK_II(5, 8.8, 15, "&7⭐⭐⭐⭐⭐", "&bSecond Generation Blade Mk. II"),
    SECOND_GEN_BLADE_MK_III(6, 9.6, 18, "&6⭐&7⭐⭐⭐⭐", "&bSecond Generation Blade Mk. III"),
    THIRD_GEN_BLADE(7, 10.5, 21, "&6⭐⭐&7⭐⭐⭐", "&eThird Generation Blade"),
    THIRD_GEN_BLADE_MK_II(8, 11.8, 24, "&6⭐⭐⭐&7⭐⭐", "&eThird Generation Blade Mk. II"),
    THIRD_GEN_BLADE_MK_III(9, 12.6, 27, "&6⭐⭐⭐⭐&7⭐", "&eThird Generation Blade Mk. III"),
    PERFECTED_BLADE(10, 13.0, 30, "&6⭐⭐⭐⭐⭐", "&5Perfected Blade");

    private final int hierarchy;
    private final double damage;
    private final int durability;

    private final String rating;
    private final String name;

    private final List<String> lore;

    BladeType(final int hierarchy, final double damage, final int durability, final String rating, final String name)
    {
        this.hierarchy = hierarchy;
        this.damage = damage;
        this.durability = durability;
        this.rating = rating;
        this.name = name;
        this.lore = Arrays.asList("", " &fRating&8: " + rating, " &fDamage&8: &7" + damage, "");
    }

    public static Optional<BladeType> find(int hierarchy)
    {
        return Arrays.stream(values()).filter(it -> it.hierarchy == (hierarchy)).findFirst();
    }

    @Override
    public int getHierarchy()
    {
        return hierarchy;
    }

    @Override
    public double getDamage()
    {
        return damage;
    }

    @Override
    public int getDurability()
    {
        return durability;
    }

    @Override
    public String getRating()
    {
        return rating;
    }

    @Override
    public String getDisplayName()
    {
        return name;
    }

    @Override
    public List<String> getLore() { return lore; }
}
