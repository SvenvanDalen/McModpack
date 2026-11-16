package com.drakenrijken.core;

/** Korte tekst over het rijk voor de speler (DR-506): niveau, macht en de volgende drempel. */
public final class RealmReport {
	private RealmReport() {}

	public static String describe(int power) {
		int level = RealmState.levelFor(power);
		int next = RealmState.nextThreshold(level);
		String volgende = next < 0 ? "hoogste niveau bereikt" : "volgend niveau bij " + next + " macht (nog " + (next - power) + ")";
		return "Rijk niveau " + level + " van " + RealmState.MAX_LEVEL + ", macht " + power + ", " + volgende + ".";
	}
}
