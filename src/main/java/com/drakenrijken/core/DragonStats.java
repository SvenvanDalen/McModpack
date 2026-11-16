package com.drakenrijken.core;

/** Wat de draak per fase doet (DR-604 tot 606). Waarden zijn eerste inschattingen om te balanceren. */
public final class DragonStats {
	public static final int BASE_HEALTH = 200;
	/** Vanaf deze fase brandt de draak spelers in de buurt aan (vuuradem, zonder eigen deeltjes). */
	public static final int FIRE_PHASE = 2;
	public static final int FIRE_INTERVAL_TICKS = 100;
	public static final int FIRE_RANGE = 6;

	private DragonStats() {}

	/** Snelheid als factor van de basis: 1.0 in fase 1, 1.15 in fase 2, 1.4 in fase 3 (woede). */
	public static double speedFactor(int phase) {
		return switch (check(phase)) { case 1 -> 1.0; case 2 -> 1.15; default -> 1.4; };
	}

	/** Schade als factor van de basis: 1.0, 1.25, 1.5. */
	public static double damageFactor(int phase) {
		return switch (check(phase)) { case 1 -> 1.0; case 2 -> 1.25; default -> 1.5; };
	}

	public static boolean breathesFire(int phase) {
		return check(phase) >= FIRE_PHASE;
	}

	private static int check(int phase) {
		if (phase < 1 || phase > 3) throw new IllegalArgumentException("fase buiten 1..3: " + phase);
		return phase;
	}
}
