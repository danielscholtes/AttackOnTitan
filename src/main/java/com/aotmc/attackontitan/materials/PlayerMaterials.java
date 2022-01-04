package com.aotmc.attackontitan.materials;

public class PlayerMaterials {

    private int smallCrystals;
    private int crystals;
    private int largeCrystals;

    public PlayerMaterials(int smallCrystals, int crystals, int largeCrystals) {
        this.smallCrystals = smallCrystals;
        this.crystals = crystals;
        this.largeCrystals = largeCrystals;
    }

    public int getCrystals() {
        return crystals;
    }

    public void setCrystals(int crystals) {
        this.crystals = crystals;
    }

    public void addCrystals(int crystals) {
        this.crystals += crystals;
    }

    public void removeCrystals(int crystals) {
        this.crystals -= crystals;
    }

    public int getSmallCrystals() {
        return smallCrystals;
    }

    public void setSmallCrystals(int crystals) {
        this.smallCrystals = crystals;
    }

    public void addSmallCrystals(int crystals) {
        this.smallCrystals += crystals;
    }

    public void removeSmallCrystals(int crystals) {
        this.smallCrystals -= crystals;
    }

    public int getLargeCrystals() {
        return largeCrystals;
    }

    public void setLargeCrystals(int crystals) {
        this.largeCrystals = crystals;
    }

    public void addLargeCrystals(int crystals) {
        this.largeCrystals += crystals;
    }

    public void removeLargeCrystals(int crystals) {
        this.largeCrystals -= crystals;
    }
}
