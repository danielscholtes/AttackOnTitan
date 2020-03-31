package com.aotmc.attackontitan.blades;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FirstGenBlade implements BaseBlade
{

    @Override
    public String getId()
    {
        return "first gen";
    }

    @Override
    public int getLevel()
    {
        return 1;
    }

    @Override
    public String getName()
    {
        return "&bFirst Gen";
    }

    @Override
    public Material getMaterial()
    {
        return Material.DIAMOND_SWORD;
    }

}
