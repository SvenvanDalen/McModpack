package com.drakenrijken.core;

/** Bepaalt de strijdfase van een eindbaas uit zijn resterende gezondheid (0.0 tot 1.0). */
public final class BossPhases {
	private BossPhases() {}

	public static int phaseFor(double healthFraction) {
		if (Double.isNaN(healthFraction) || healthFraction < 0 || healthFraction > 1) {
			throw new IllegalArgumentException("gezondheid buiten 0..1: " + healthFraction);
		}
		if (healthFraction > 2.0 / 3) {
			return 1;
		}
		return healthFraction > 1.0 / 3 ? 2 : 3;
	}
}
