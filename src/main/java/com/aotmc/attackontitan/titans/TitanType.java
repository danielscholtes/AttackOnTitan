package com.aotmc.attackontitan.titans;

import com.aotmc.attackontitan.AttackOnTitan;

import java.util.Random;

public enum TitanType {

	SMALL(),
	MEDIUM(),
	LARGE();

	private static final TitanType[] VALUES = values();
	private static final int SIZE = VALUES.length;
	private static final Random RANDOM = AttackOnTitan.getInstance().getRandom();

	public static TitanType getRandomSize()  {
		return VALUES[RANDOM.nextInt(SIZE)];
	}
	
}
