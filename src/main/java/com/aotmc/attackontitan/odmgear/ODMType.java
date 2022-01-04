package com.aotmc.attackontitan.odmgear;

import com.aotmc.attackontitan.GearType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum ODMType implements GearType {

    FIRST_GEN_ODM(1, 500, "&7⭐", "&fFirst Generation ODM", 0, 0, 0, 0),
    FIRST_GEN_ODM_MK_II(2, 950, "&7⭐⭐", "&fFirst Generation ODM Mk. II", 2, 5, 0, 100),
    FIRST_GEN_ODM_MK_III(3, 1400, "&7⭐⭐⭐", "&fFirst Generation ODM Mk. III", 4, 10, 0, 150),
    SECOND_GEN_ODM(4, 1850, "&7⭐⭐⭐⭐", "&bSecond Generation ODM", 6, 15, 3, 200),
    SECOND_GEN_ODM_MK_II(5, 2300, "&7⭐⭐⭐⭐⭐", "&bSecond Generation ODM Mk. II", 8, 20, 9, 250),
    SECOND_GEN_ODM_MK_III(6, 2750, "&6⭐&7⭐⭐⭐⭐", "&bSecond Generation ODM Mk. III", 10, 25, 9, 300),
    THIRD_GEN_ODM(7, 3200, "&6⭐⭐&7⭐⭐⭐", "&eThird Generation ODM", 12, 30, 15, 350),
    THIRD_GEN_ODM_MK_II(8, 3650, "&6⭐⭐⭐&7⭐⭐", "&eThird Generation ODM Mk. II", 14, 35, 18, 400),
    THIRD_GEN_ODM_MK_III(9, 4100, "&6⭐⭐⭐⭐&7⭐", "&eThird Generation ODM Mk. III", 16, 40, 21, 450),
    PERFECTED_ODM(10, 5000, "&6⭐⭐⭐⭐⭐", "&5Perfected ODM", 20, 50, 30, 500);

    private final int capacity;
    private final int hierarchy;

    private final int smallCrystals;
    private final int crystals;
    private final int largeCrystals;
    private final int price;

    private final String rating;
    private final String name;

    private final List<String> lore;

    ODMType(final int hierarchy, final int capacity, final String rating, final String name, int smallCrystals, int crystals, int largeCrystals, int price)
    {
        this.capacity = capacity;
        this.hierarchy = hierarchy;
        this.rating = rating;
        this.name = name;
        this.lore = Arrays.asList("", " &fRating&8: " + rating);
        this.smallCrystals = smallCrystals;
        this.crystals = crystals;
        this.largeCrystals = largeCrystals;
        this.price = price;
    }

    public static Optional<ODMType> find(int hierarchy)
    {
        return Arrays.stream(values()).filter(it -> it.hierarchy == (hierarchy)).findFirst();
    }

    @Override
    public int getHierarchy() {
        return hierarchy;
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

    public int getGasCapacity() {
        return capacity;
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

