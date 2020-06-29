package com.aotmc.attackontitan.blades;

import java.util.List;

public interface BladeConstructor
{
    int getHierarchy();
    double getDamage();
    int getDurability();

    String getRating();
    String getDisplayName();

    List<String> getLore();

}
