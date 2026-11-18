package com.drakenrijken.core;

/**
 * Belegeringswapens zelf bouwen (DR-1105): het ontwerp bepaalt bereik, schade en gewicht.
 * Een langere arm geeft meer bereik, een zwaarder contragewicht meer schade; beide maken het wapen zwaarder.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class SiegeWeapon {
	public static final int MAX_ARM = 12;
	public static final int MAX_COUNTERWEIGHT = 12;
	public static final int MIN_PART = 1;
	/** Boven dit gewicht kan het wapen niet meer verplaatst worden. */
	public static final int MAX_MOVABLE_WEIGHT = 40;

	/** arm = lengte van de arm, counterweight = zwaarte van het contragewicht, frame = stevigheid van het frame (alle in blokken). */
	public record Design(int arm, int counterweight, int frame) {
		public Design {
			if (arm < MIN_PART || arm > MAX_ARM) throw new IllegalArgumentException("arm buiten 1.." + MAX_ARM + ": " + arm);
			if (counterweight < MIN_PART || counterweight > MAX_COUNTERWEIGHT) throw new IllegalArgumentException("contragewicht buiten 1.." + MAX_COUNTERWEIGHT + ": " + counterweight);
			if (frame < MIN_PART) throw new IllegalArgumentException("frame te klein: " + frame);
		}
	}

	private SiegeWeapon() {}

	public static int range(Design d) { return 8 + d.arm() * 4; }

	public static int damage(Design d) { return 2 + d.counterweight() * 2; }

	public static int weight(Design d) { return d.arm() + d.counterweight() * 2 + d.frame(); }

	/** Een te licht frame breekt onder de kracht van het contragewicht. */
	public static boolean sturdy(Design d) { return d.frame() * 2 >= d.counterweight(); }

	public static boolean movable(Design d) { return weight(d) <= MAX_MOVABLE_WEIGHT; }

	public static boolean usable(Design d) { return sturdy(d) && movable(d); }
}
