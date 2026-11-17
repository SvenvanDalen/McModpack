package com.drakenrijken.core;

/** Wat de Runenwachter doet per fase (DR-610b). Eerste inschattingen om te balanceren. */
public final class WardenStats {
	public static final int BASE_HEALTH = 150;
	public static final int SHOCK_RANGE = 5;
	public static final float SHOCK_DAMAGE = 8f;

	private WardenStats() {}

	/** Ticks tussen twee schokgolven: 100, 80, 50 voor fase 1, 2, 3. */
	public static int shockInterval(int phase) {
		return switch (check(phase)) { case 1 -> 100; case 2 -> 80; default -> 50; };
	}

	/** Schokgolf-schade als factor: 1.0, 1.25, 1.5. */
	public static double damageFactor(int phase) {
		return switch (check(phase)) { case 1 -> 1.0; case 2 -> 1.25; default -> 1.5; };
	}

	private static int check(int phase) {
		if (phase < 1 || phase > 3) throw new IllegalArgumentException("fase buiten 1..3: " + phase);
		return phase;
	}
}
