package com.aotmc.attackontitan.blades;

public enum Blades {
	
	FIRST_GEN_BLADE(1, 10, 10),
	FIRST_GEN_BLADE_MK_II(1, 10, 10),
	FIRST_GEN_BLADE_MK_III(1, 10, 10),
	SECOND_GEN_BLADE_MK_II(1, 10, 10),
	SECOND_GEN_BLADE_MK_III(1, 10, 10),
	SECOND_GEN_BLADE(1, 10, 10);
	
	public final int modelData;
	public final double damage;
	public final double durability;
	 
    private Blades(int modelData, double damage, double durability) {
        this.modelData = modelData;
        this.damage = damage;
        this.durability = durability;
    }
    
    public int getModelData() {
    	return modelData;
    }
    
    public double getDamage() {
    	return damage;
    }
    
    public double getDurability() {
    	return durability;
    }
	
}
