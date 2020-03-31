package com.aotmc.attackontitan.blades;

import java.util.ArrayList;
import java.util.List;

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

	public List<String> getDefaultLore(final String blade, final double damage, final double durability)
	{
		List<String> lore = new ArrayList<>();

		switch (blade)
		{
			case "FIRST_GEN_BLADE":
			case "FIRST_GEN_BLADE_MK_II":
			case "FIRST_GEN_BLADE_MK_III":
				lore.add("");
				lore.add(" &8• &7Damage&8: &f" + damage);
				lore.add(" &8• &7Durability&8: &f" + durability);
				lore.add(" &8• &7Rarity&8: &fCommon");
				break;
			case "SECOND_GEN_BLADE":
			case "SECOND_GEN_BLADE_MK_II":
			case "SECOND_GEN_BLADE_MK_III":
				lore.add("");
				lore.add(" &8• &7Damage&8: &f" + damage);
				lore.add(" &8• &7Durability&8: &f" + durability);
				lore.add(" &8• &7Rarity&8: &fUn-Common");
				break;
			case "THIRD_GEN_BLADE":
			case "THIRD_GEN_BLADE_MK_II":
			case "THIRD_GEN_BLADE_MK_III":
				lore.add("");
				lore.add(" &8• &7Damage&8: &f" + damage);
				lore.add(" &8• &7Durability&8: &f" + durability);
				lore.add(" &8• &7Rarity&8: &fRare");
				break;
			case "PRESTIGED_BLADE":
				lore.add("");
				lore.add(" &8• &7Damage&8: &f" + damage);
				lore.add(" &8• &7Durability&8: &f" + durability);
				lore.add(" &8• &7Rarity&8: &fEpic");
		}

		return lore;
	}
}
