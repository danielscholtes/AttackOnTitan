package com.aotmc.attackontitan.music;

import java.util.Random;

import com.aotmc.attackontitan.AttackOnTitan;

public enum Music {

	THE_DISCIPLINE(444, "thediscipline"), 
	HOW_DO_YOU_LIKE_LIVING_INSIDE(352, "howdoyoulikelivinginside"),
	INSIDE_THE_WALL_NIGHT(441, "insidethewallnight");

	private final int duration;
	private final String name;

	private static final Music[] VALUES = values();	
	private static final int SIZE = VALUES.length;
	private static final Random RANDOM = AttackOnTitan.getInstance().getRandom();

	Music(final int duration, final String name) {
        this.duration = duration;
        this.name = name;
    }

	public int getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}
	
	public static Music getRandomMusic()  {
		return VALUES[RANDOM.nextInt(SIZE)];
	}

}
