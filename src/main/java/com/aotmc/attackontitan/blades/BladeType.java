package com.aotmc.attackontitan.blades;

import com.aotmc.attackontitan.GearType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum BladeType implements GearType {

    FIRST_GEN_BLADE(1, 50.0,"&7⭐", "&fFirst Generation Blade", 0, 0, 0, 0),
    FIRST_GEN_BLADE_MK_II(2, 80.0, "&7⭐⭐", "&fFirst Generation Blade Mk. II", 10, 1, 0, 100),
    FIRST_GEN_BLADE_MK_III(3, 110.0, "&7⭐⭐⭐", "&fFirst Generation Blade Mk. III", 16, 2, 0, 150),
    SECOND_GEN_BLADE(4, 160.0, "&7⭐⭐⭐⭐", "&bSecond Generation Blade", 15, 5, 1, 200),
    SECOND_GEN_BLADE_MK_II(5, 200.0, "&7⭐⭐⭐⭐⭐", "&bSecond Generation Blade Mk. II", 40, 11, 3, 250),
    SECOND_GEN_BLADE_MK_III(6, 250.0, "&6⭐&7⭐⭐⭐⭐", "&bSecond Generation Blade Mk. III", 65, 25, 7, 300),
    THIRD_GEN_BLADE(7, 350.0, "&6⭐⭐&7⭐⭐⭐", "&eThird Generation Blade", 104, 57, 20, 350),
    THIRD_GEN_BLADE_MK_II(8, 420.0, "&6⭐⭐⭐&7⭐⭐", "&eThird Generation Blade Mk. II", 167, 129, 57, 400),
    THIRD_GEN_BLADE_MK_III(9, 500.0, "&6⭐⭐⭐⭐&7⭐", "&eThird Generation Blade Mk. III", 268, 200, 157, 450),
    PERFECTED_BLADE(10, 750.0, "&6⭐⭐⭐⭐⭐", "&5Perfected Blade", 500, 400, 200, 500);

    private final double damage;
    private final int hierarchy;

    private final int smallCrystals;
    private final int crystals;
    private final int largeCrystals;
    private final int price;

    private final String rating;
    private final String name;

    private final List<String> lore;

    BladeType(final int hierarchy, final double damage, final String rating, final String name, int smallCrystals, int crystals, int largeCrystals, int price)
    {
        this.damage = damage;
        this.hierarchy = hierarchy;
        this.rating = rating;
        this.name = name;
        this.lore = Arrays.asList("", " &fRating&8: " + rating, " &fDamage&8: &7" + damage, "");
        this.smallCrystals = smallCrystals;
        this.crystals = crystals;
        this.largeCrystals = largeCrystals;
        this.price = price;
    }

    public static Optional<BladeType> find(int hierarchy)
    {
        return Arrays.stream(values()).filter(it -> it.hierarchy == (hierarchy)).findFirst();
    }

    @Override
    public int getSmallCrystals() {
        return smallCrystals;
    }

    @Override
    public int getCrystals() {
        return crystals;
    }

    @Override
    public int getLargeCrystals() {
        return largeCrystals;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getHierarchy()
    {
        return hierarchy;
    }

    public double getDamage()
    {
        return damage;
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
