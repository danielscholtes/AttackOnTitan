package com.aotmc.attackontitan;

import java.util.List;

public interface GearType {

    int getHierarchy();

    int getSmallCrystals();
    int getCrystals();
    int getLargeCrystals();
    int getPrice();

    String getRating();
    String getDisplayName();

    List<String> getLore();

}
