package com.drakenrijken.core;

/** Thema van een koninkrijk: westers (bestaand) of Japans (ongeveer 1 op 4). Deterministisch uit seed en chunk, met een eigen zout zodat bestaande koninkrijken niet verschuiven. */
public enum KingdomTheme {
	WESTERS, JAPANS;

	private static final long SALT = 0x4A4150414E533031L;

	public static KingdomTheme of(long seed, int chunkX, int chunkZ) {
		long h = mix(seed ^ SALT ^ mix(chunkX * 0xD1B54A32D192ED03L + chunkZ * 0x8CB92BA72F3D8DD7L + 0x2545F4914F6CDD1DL));
		return Long.remainderUnsigned(h, 4) == 0 ? JAPANS : WESTERS;
	}

	/** Of dit koninkrijk een grote stad krijgt: nooit bij een Japans koninkrijk, daar gaat de Japanse bouw voor. */
	public static boolean usesCity(long seed, int chunkX, int chunkZ) {
		return of(seed, chunkX, chunkZ) != JAPANS && CityPlans.isCity(seed, chunkX, chunkZ);
	}

	/** SplitMix64-menging, zoals in KingdomPlacement. */
	private static long mix(long z) {
		z += 0x9E3779B97F4A7C15L;
		z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
		z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
		return z ^ (z >>> 31);
	}
}
