package com.drakenrijken.core;

/** Belasting van het rijk (#154): elke speldag goud, met een plafond van 3 dagen. Waarden zijn eerste inschattingen om te balanceren. */
public final class RealmTax {
	/** Zoveel dagen belasting blijft hoogstens liggen. */
	public static final int MAX_DAYS = 3;

	private RealmTax() {}

	/** Goudstaven per dag: 0 bij niveau 0, 2 per niveau, 10 bij niveau 5. */
	public static int daily(int level) {
		if (level < 0 || level > RealmState.MAX_LEVEL) {
			throw new IllegalArgumentException("rijksniveau buiten 0.." + RealmState.MAX_LEVEL + ": " + level);
		}
		return 2 * level;
	}

	/** Wat er klaarligt sinds de laatste inning, met het plafond van 3 dagen. */
	public static int owed(int level, long lastCollectedDay, long today) {
		long dagen = Math.max(0, Math.min(MAX_DAYS, today - lastCollectedDay));
		return (int) (daily(level) * dagen);
	}
}
