package com.drakenrijken.core;

/** Rijksniveau (0 tot 5) uit de totale macht. */
public final class RealmState {
	public static final int MAX_LEVEL = 5;
	/** Minimale macht voor niveau 1..5. */
	private static final int[] THRESHOLDS = {10, 50, 150, 400, 1000};

	private RealmState() {}

	public static int levelFor(int power) {
		if (power < 0) {
			throw new IllegalArgumentException("macht is negatief: " + power);
		}
		int level = 0;
		while (level < MAX_LEVEL && power >= THRESHOLDS[level]) {
			level++;
		}
		return level;
	}

	/** Macht die nodig is voor het volgende niveau, of -1 op het hoogste niveau. */
	public static int nextThreshold(int level) {
		return level >= MAX_LEVEL ? -1 : THRESHOLDS[Math.max(level, 0)];
	}
}
