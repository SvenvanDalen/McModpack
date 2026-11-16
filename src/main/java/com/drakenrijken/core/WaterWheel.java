package com.drakenrijken.core;

/** Waterrad (tijdperk 1): levert kracht naar het aantal zijden waar stromend water langs komt. Waarden zijn eerste inschattingen. */
public final class WaterWheel {
	public static final int POWER_PER_SIDE = 2;
	public static final int MAX_SIDES = 4;

	private WaterWheel() {}

	/** Kracht bij dit aantal zijden met stromend water (0 tot 4); geen water geeft 0. */
	public static int power(int flowingSides) {
		if (flowingSides < 0 || flowingSides > MAX_SIDES) {
			throw new IllegalArgumentException("zijden buiten 0.." + MAX_SIDES + ": " + flowingSides);
		}
		return flowingSides * POWER_PER_SIDE;
	}
}
