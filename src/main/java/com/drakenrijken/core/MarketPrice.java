package com.drakenrijken.core;

/**
 * Marktprijs die per speldag schommelt (#156). Een vaste reeks uit het dagnummer, dus zonder opslag en voor
 * iedereen gelijk. Vraag en aanbod door verkopen van de speler zit er nog niet in. Waarden zijn eerste inschattingen.
 */
public final class MarketPrice {
	/** De dagfactor ligt tussen 85 en 115 procent van de basisprijs. */
	public static final int MIN_PERCENT = 85;
	public static final int MAX_PERCENT = 115;
	/** Absolute grenzen, ook samen met de rijkskorting: nooit onder 50 of boven 150 procent. */
	public static final int FLOOR_PERCENT = 50;
	public static final int CEIL_PERCENT = 150;
	private static final int SPAN = MAX_PERCENT - MIN_PERCENT + 1;

	private MarketPrice() {}

	/** Dagfactor in procenten (85 tot 115) voor een speldag. */
	public static int dayPercent(long day) {
		if (day < 0) throw new IllegalArgumentException("dag is negatief: " + day);
		return MIN_PERCENT + (int) ((day * 37 + 11) % SPAN);
	}

	/** Prijs in procenten van de basisprijs na rijkskorting en dagfactor, begrensd op 50 tot 150. */
	public static int totalPercent(int realmLevel, long day) {
		int p = RealmEffects.pricePercent(realmLevel) * dayPercent(day) / 100;
		return Math.max(FLOOR_PERCENT, Math.min(CEIL_PERCENT, p));
	}

	/** Extra of minder welwillendheid voor de handel: een dure dag (boven 100) kost tot 3 punten, een goedkope dag levert ze op. */
	public static int goodwillShift(long day) {
		return (100 - dayPercent(day)) * 20 / 100;
	}
}
