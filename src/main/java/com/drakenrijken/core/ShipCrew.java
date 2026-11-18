package com.drakenrijken.core;

/**
 * Scheepsbemanning en kanonnen (DR-1107): zonder bemanning vaart het schip niet.
 * Kanonnen en bemanning delen de plekken van het schip ({@link ShipDesign#slots}).
 */
public final class ShipCrew {
	/** Minimale bemanning om te varen. */
	public static final int MIN_SAILORS = 2;
	/** Zoveel bemanning heeft één kanon nodig om te schieten. */
	public static final int GUNNERS_PER_CANNON = 2;

	private ShipCrew() {}

	public static boolean canSail(int sailors) {
		if (sailors < 0) throw new IllegalArgumentException("negatief aantal: " + sailors);
		return sailors >= MIN_SAILORS;
	}

	/** Bemanning en kanonnen passen samen op de plekken. */
	public static boolean fits(int slots, int sailors, int cannons) {
		if (slots < 0 || sailors < 0 || cannons < 0) throw new IllegalArgumentException("negatief aantal");
		return sailors + cannons <= slots;
	}

	/** Kanonnen die echt kunnen schieten: bemanning boven het minimum, per kanon twee schutters. */
	public static int firingCannons(int sailors, int cannons) {
		if (sailors < 0 || cannons < 0) throw new IllegalArgumentException("negatief aantal");
		if (!canSail(sailors)) return 0;
		return Math.min(cannons, (sailors - MIN_SAILORS) / GUNNERS_PER_CANNON);
	}

	/** Snelheid van het schip met deze bemanning: nul zonder minimale bemanning. */
	public static int speed(ShipDesign.Design design, int sailors) {
		return canSail(sailors) ? ShipDesign.speed(design) : 0;
	}
}
