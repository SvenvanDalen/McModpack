package com.drakenrijken.core;

/**
 * Groei van AI-koninkrijken (DR-1101): elk koninkrijk start met een eigen grootte en groeit, blijft gelijk of vervalt.
 * Alles volgt deterministisch uit seed, plek en dag; er is geen opslag nodig. Niveau 0 is een verwoest rijk.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class KingdomGrowth {
	public static final int RUINED = 0;
	public static final int MAX_LEVEL = 5;
	/** Aantal dagen tussen twee stappen van groei of verval. */
	public static final int DAYS_PER_STEP = 20;
	public static final int INHABITANTS_PER_LEVEL = 40;

	public enum Trend { GROWING, STABLE, DECLINING }

	private KingdomGrowth() {}

	private static long hash(long seed, int cx, int cz) {
		long z = seed ^ (cx * 0x9E3779B97F4A7C15L) ^ (cz * 0xC2B2AE3D27D4EB4FL) ^ 0x1234ABCDL;
		z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
		z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
		return z ^ (z >>> 31);
	}

	/** Beginniveau (2 tot 4) van dit koninkrijk op dag 0. */
	public static int startLevel(long seed, int cx, int cz) {
		return 2 + (int) Long.remainderUnsigned(hash(seed, cx, cz), 3);
	}

	public static Trend trend(long seed, int cx, int cz) {
		return Trend.values()[(int) Long.remainderUnsigned(hash(seed, cx, cz) >>> 8, 3)];
	}

	/** Niveau (0 tot {@value #MAX_LEVEL}) op een speldag; negatieve dagen tellen als dag 0. */
	public static int level(long seed, int cx, int cz, long day) {
		long steps = Math.max(0, day) / DAYS_PER_STEP;
		long delta = switch (trend(seed, cx, cz)) {
			case GROWING -> steps;
			case STABLE -> 0;
			case DECLINING -> -steps;
		};
		return (int) Math.max(RUINED, Math.min(MAX_LEVEL, startLevel(seed, cx, cz) + delta));
	}

	public static boolean isRuined(long seed, int cx, int cz, long day) {
		return level(seed, cx, cz, day) == RUINED;
	}

	public static int inhabitants(int level) {
		return Math.max(RUINED, Math.min(MAX_LEVEL, level)) * INHABITANTS_PER_LEVEL;
	}
}
