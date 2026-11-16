package com.drakenrijken.core;

/** Welwillendheid van handelaren bij een rijksniveau (DR-504): 4 punten per niveau, 20 bij niveau 5. */
public final class RealmTrade {
	public static final int PER_LEVEL = 4;

	private RealmTrade() {}

	public static int goodwill(int level) {
		if (level < 0 || level > RealmState.MAX_LEVEL) {
			throw new IllegalArgumentException("rijksniveau buiten 0.." + RealmState.MAX_LEVEL + ": " + level);
		}
		return PER_LEVEL * level;
	}
}
