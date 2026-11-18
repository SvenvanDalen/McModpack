package com.drakenrijken.core;

/** Seizoenen (#155): een cyclus van 4 x 7 speldagen. De oogstfactor is een eerste inschatting en nog niet aan de oogst gekoppeld. */
public enum Season {
	LENTE("Lente", 100),
	ZOMER("Zomer", 130),
	HERFST("Herfst", 115),
	WINTER("Winter", 60);

	public static final int DAYS_PER_SEASON = 7;
	public static final int CYCLE_DAYS = DAYS_PER_SEASON * 4;

	private final String naam;
	private final int harvestPercent;

	Season(String naam, int harvestPercent) {
		this.naam = naam;
		this.harvestPercent = harvestPercent;
	}

	public String naam() { return naam; }

	public int harvestPercent() { return harvestPercent; }

	/** Het seizoen op deze speldag (dag 0 is de eerste lentedag); negatieve dagen tellen als 0. */
	public static Season of(long day) {
		long d = Math.max(0, day);
		return values()[(int) (d % CYCLE_DAYS / DAYS_PER_SEASON)];
	}

	/** Hoeveel dagen dit seizoen nog duurt, de huidige dag meegeteld (1 tot 7). */
	public static int daysLeft(long day) {
		long d = Math.max(0, day);
		return DAYS_PER_SEASON - (int) (d % DAYS_PER_SEASON);
	}
}
