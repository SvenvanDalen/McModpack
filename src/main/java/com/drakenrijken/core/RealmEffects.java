package com.drakenrijken.core;

/** Gevolgen van het rijksniveau (0 tot 5) voor de speler. Waarden zijn eerste inschattingen om te balanceren. */
public final class RealmEffects {
	private RealmEffects() {}

	/** Prijs in procenten van de normale prijs: 100 bij niveau 0, 5 procent lager per niveau, 75 bij niveau 5. */
	public static int pricePercent(int level) {
		return 100 - 5 * check(level);
	}

	/** Prijs voor de speler, nooit lager dan 1. */
	public static int price(int basePrice, int level) {
		if (basePrice < 0) throw new IllegalArgumentException("prijs is negatief: " + basePrice);
		if (basePrice == 0) return 0;
		return (int) Math.max(1, (long) basePrice * pricePercent(level) / 100);
	}

	/** Hoeveel dorpelingen het rijk huisvest: 4 bij niveau 0, 4 erbij per niveau, 24 bij niveau 5. */
	public static int villagerCap(int level) {
		return 4 + 4 * check(level);
	}

	private static int check(int level) {
		if (level < 0 || level > RealmState.MAX_LEVEL) {
			throw new IllegalArgumentException("rijksniveau buiten 0.." + RealmState.MAX_LEVEL + ": " + level);
		}
		return level;
	}
}
