package com.drakenrijken.core;

/** Vijandelijke golven op hogere rijksniveaus (DR-505). Waarden zijn eerste inschattingen om te balanceren. */
public final class RealmRaids {
	/** Vanaf dit rijksniveau komen er aanvallen. */
	public static final int FIRST_RAID_LEVEL = 2;
	/** Nooit meer vijanden in één golf dan dit, ook niet bij een vreemd niveau. */
	public static final int MAX_WAVE_SIZE = EntityBudget.MAX_ENEMIES / 2;

	private RealmRaids() {}

	public static boolean raidsAt(int level) {
		return check(level) >= FIRST_RAID_LEVEL;
	}

	/** Aantal vijanden in een golf: 0 onder niveau 2, daarna 3 plus 2 per niveau boven 2 (3, 5, 7, 9). */
	public static int waveSize(int level) {
		if (!raidsAt(level)) return 0;
		return Math.min(MAX_WAVE_SIZE, 3 + 2 * (level - FIRST_RAID_LEVEL));
	}

	/** Aantal golven per aanval: 1 tot niveau 3, 2 vanaf niveau 4. */
	public static int waves(int level) {
		if (!raidsAt(level)) return 0;
		return level >= 4 ? 2 : 1;
	}

	/** Tijdperk van de aanvallers: hoogstens het tijdperk dat het rijk aankan (niveau 2-3 = 1, 4 = 2, 5 = 3). */
	public static int enemyEra(int level) {
		if (!raidsAt(level)) return 0;
		return level >= 5 ? 3 : level >= 4 ? 2 : 1;
	}

	private static int check(int level) {
		if (level < 0 || level > RealmState.MAX_LEVEL) {
			throw new IllegalArgumentException("rijksniveau buiten 0.." + RealmState.MAX_LEVEL + ": " + level);
		}
		return level;
	}
}
