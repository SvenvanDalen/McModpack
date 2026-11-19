package com.drakenrijken.core;

/**
 * Schip zelf bouwen (DR-1106): kiel en ontwerp bepalen drijfkracht, snelheid, wendbaarheid en plekken.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class ShipDesign {
	public static final int MIN_KEEL = 3;
	public static final int MAX_KEEL = 40;
	public static final int BLOCK_WEIGHT = 1;

	/** keel = lengte van de kiel, width = breedte, hull = aantal rompblokken, sails = aantal zeilblokken. */
	public record Design(int keel, int width, int hull, int sails) {
		public Design {
			if (keel < MIN_KEEL || keel > MAX_KEEL) throw new IllegalArgumentException("kiel buiten " + MIN_KEEL + ".." + MAX_KEEL + ": " + keel);
			if (width < 1 || width > keel) throw new IllegalArgumentException("breedte buiten 1.." + keel + ": " + width);
			if (hull < 0 || sails < 0) throw new IllegalArgumentException("negatief aantal blokken");
		}
	}

	private ShipDesign() {}

	/** Drijfkracht: hoe groot de romp onder water is (lengte x breedte). */
	public static int buoyancy(Design d) { return d.keel() * d.width() * 2; }

	public static int weight(Design d) { return (d.hull() + d.sails()) * BLOCK_WEIGHT; }

	public static boolean floats(Design d) { return weight(d) <= buoyancy(d); }

	/** Meer zeil geeft snelheid, een zwaar schip vaart trager. Nul als het zinkt. */
	public static int speed(Design d) {
		if (!floats(d)) return 0;
		return Math.max(1, d.sails() * 2 - weight(d) / 10);
	}

	/** Korte, smalle schepen draaien beter dan lange, brede. */
	public static int maneuverability(Design d) { return Math.max(1, 20 - d.keel() / 2 - d.width()); }

	/** Plekken voor bemanning en kanonnen. */
	public static int slots(Design d) { return d.keel() * d.width() / 4; }
}
