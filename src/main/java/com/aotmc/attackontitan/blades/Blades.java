package com.aotmc.attackontitan.blades;

public enum Blades {
	
	FIRST_GEN_BLADE(1, 10, 10),
	FIRST_GEN_BLADE_MK_II(1, 10, 10),
	FIRST_GEN_BLADE_MK_III(1, 10, 10),
	SECOND_GEN_BLADE_MK_II(1, 10, 10),
	SECOND_GEN_BLADE_MK_III(1, 10, 10),
	SECOND_GEN_BLADE(1, 10, 10),
	THIRD_GEN_BLADE(1,10,10),
	THIRD_GEN_BLADE_MK_II(1,10,10),
	THIRD_GEN_BLADE_MK_III(1,10,10),
	PRESTIGED_BLADE(1,10,10);
	
	public final int modelData;
	public final double damage;
	public final double durability;
	 
    Blades(final int modelData, final double damage, final double durability) {
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

    public String getFormattedName(final String blade)
	{
		switch(blade)
		{
			case "FIRST_GEN_BLADE":
				return "&fFirst Gen Blade";
			case "FIRST_GEN_BLADE_MK_II":
				return "&fFirst Gen Blade Mk. II";
			case "FIRST_GEN_BLADE_MK_III":
				return "&fFirst Gen Blade Mk. III";
			case "SECOND_GEN_BLADE":
				return "&bSecond Gen Blade";
			case "SECOND_GEN_BLADE_MK_II":
				return "&bSecond Gen Blade Mk. II";
			case "SECOND_GEN_BLADE_MK_III":
				return "&bSecond Gen Blade Mk. III";
			case "THIRD_GEN_BLADE":
				return "&dThird Gen Blade";
			case "THIRD_GEN_BLADE_MK_II":
				return "&dThird Gen Blade Mk. II";
			case "THIRD_GEN_BLADE_MK_III":
				return "&dThird Gen Blade Mk. III";
			case "PRESTIGED_BLADE":
				return "&5Prestiged Blade";
			default:
				return "Invalid Blade";
		}
	}
}
