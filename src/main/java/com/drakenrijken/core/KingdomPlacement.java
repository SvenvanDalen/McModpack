package com.drakenrijken.core;

/** Bepaalt deterministisch welke chunks een koninkrijk bevatten: zelfde seed en chunk geven altijd hetzelfde antwoord. */
public final class KingdomPlacement {
	/** Kans per chunk (1%); bij 10.000 chunks liggen er dus ongeveer 100 koninkrijken. */
	public static final double DENSITY = 0.01;

	private KingdomPlacement() {}

	public static boolean hasKingdom(long seed, int chunkX, int chunkZ) {
		long h = mix(seed ^ mix(chunkX * 0x9E3779B97F4A7C15L) ^ mix(chunkZ * 0xC2B2AE3D27D4EB4FL + 0x165667B19E3779F9L));
		double unit = (h >>> 11) * 0x1.0p-53;
		return unit < DENSITY;
	}

	/** SplitMix64-menging: kleine verschillen in invoer geven sterk verschillende uitvoer. */
	private static long mix(long z) {
		z += 0x9E3779B97F4A7C15L;
		z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
		z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
		return z ^ (z >>> 31);
	}
}
