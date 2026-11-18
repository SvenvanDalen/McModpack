package com.drakenrijken.core;

/**
 * Koning omkopen of afzetten (DR-1108): daarna neem je zelf zijn plek in via je eigen rijk.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class KingTakeover {
	public static final int BRIBE_PER_LEVEL = 200;
	public static final int GUARD_STRENGTH_PER_LEVEL = 40;

	private KingTakeover() {}

	private static void check(int level) {
		if (level < 1 || level > KingdomGrowth.MAX_LEVEL) throw new IllegalArgumentException("koninkrijksniveau buiten 1.." + KingdomGrowth.MAX_LEVEL + ": " + level);
	}

	/** Prijs om de koning om te kopen; hoger niveau is duurder. */
	public static int bribeCost(int kingdomLevel) {
		check(kingdomLevel);
		return kingdomLevel * BRIBE_PER_LEVEL;
	}

	public static boolean canBribe(int kingdomLevel, int gold) {
		if (gold < 0) throw new IllegalArgumentException("negatief goud: " + gold);
		return gold >= bribeCost(kingdomLevel);
	}

	/** Sterkte van de koninklijke wacht. */
	public static int guardStrength(int kingdomLevel) {
		check(kingdomLevel);
		return kingdomLevel * GUARD_STRENGTH_PER_LEVEL;
	}

	/** Afzetten lukt als je leger (zie {@link Army#strength}) sterker is dan de wacht. */
	public static boolean canDepose(int kingdomLevel, int armyStrength) {
		if (armyStrength < 0) throw new IllegalArgumentException("negatieve sterkte: " + armyStrength);
		return armyStrength > guardStrength(kingdomLevel);
	}

	/** Zijn plek innemen kan alleen met een eigen rijk dat minstens even groot is als het koninkrijk. */
	public static boolean canTakeThrone(int kingdomLevel, int ownRealmLevel) {
		check(kingdomLevel);
		return ownRealmLevel >= kingdomLevel;
	}
}
