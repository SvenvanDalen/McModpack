package com.drakenrijken.core;

/** Windmolen (tijdperk 1): kracht groeit met de hoogte en vraagt vrije lucht. Waarden zijn eerste inschattingen (#145). */
public final class WindMill {
	public static final int MIN_HEIGHT = 64;
	public static final int MAX_HEIGHT = 200;
	public static final int MAX_POWER = 6;

	private WindMill() {}

	/** 0 onder hoogte 64 of zonder vrije lucht; daarboven lineair tot 6 op hoogte 200. */
	public static int power(int height, boolean freeAir) {
		if (!freeAir || height <= MIN_HEIGHT) return 0;
		int h = Math.min(height, MAX_HEIGHT);
		return (h - MIN_HEIGHT) * MAX_POWER / (MAX_HEIGHT - MIN_HEIGHT);
	}
}
