package com.drakenrijken.core;

/**
 * Opstanden (DR-1110): slechte behandeling van gevangenen leidt tot opstand.
 * Onrust loopt op bij hard werk, honger en te weinig bewakers; goede behandeling laat haar zakken.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class Unrest {
	public static final int MAX = 100;
	/** Vanaf deze onrust breekt een opstand uit. */
	public static final int REVOLT_AT = 80;

	private Unrest() {}

	/** Onrust na één dag: +10 bij honger, +10 als er te weinig bewakers zijn, +5 bij hard werk, anders -10. */
	public static int next(int unrest, boolean fed, boolean enoughGuards, boolean hardWork) {
		if (unrest < 0 || unrest > MAX) throw new IllegalArgumentException("onrust buiten 0.." + MAX + ": " + unrest);
		int change = 0;
		if (!fed) change += 10;
		if (!enoughGuards) change += 10;
		if (hardWork) change += 5;
		if (change == 0) change = -10;
		return Math.max(0, Math.min(MAX, unrest + change));
	}

	public static boolean revolts(int unrest) { return unrest >= REVOLT_AT; }

	/** Bij opstand vluchten of vechten alle gevangenen: het werk valt stil en het leger verliest bewakers. */
	public static int guardsLost(int guards, int captives) {
		if (guards < 0 || captives < 0) throw new IllegalArgumentException("negatief aantal");
		return Math.min(guards, captives / 4);
	}
}
