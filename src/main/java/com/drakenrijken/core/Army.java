package com.drakenrijken.core;

/**
 * Leger (DR-1104): soldiers aanwerven, uitrusten en voeden. Alle soldiers tellen mee in de sterkte,
 * maar zichtbaar zijn er hooguit {@link #MAX_VISIBLE} (binnen {@link EntityBudget}), zodat het spel snel blijft.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class Army {
	public static final int MAX_SOLDIERS = 200;
	public static final int MAX_VISIBLE = EntityBudget.MAX_NPCS;
	public static final int FOOD_PER_SOLDIER_PER_DAY = 1;

	public enum Gear {
		NONE(1), LEATHER(2), IRON(3), STEEL(5);

		private final int strength;

		Gear(int strength) { this.strength = strength; }

		public int strength() { return strength; }
	}

	private Army() {}

	/** Aantal soldaten na aanwerven; nooit boven het maximum. */
	public static int recruit(int current, int wanted) {
		if (current < 0 || wanted < 0) throw new IllegalArgumentException("negatief aantal");
		return Math.min(MAX_SOLDIERS, current + wanted);
	}

	/** Voedsel dat het leger per dag nodig heeft. */
	public static int foodNeeded(int soldiers) {
		if (soldiers < 0) throw new IllegalArgumentException("negatief aantal: " + soldiers);
		return soldiers * FOOD_PER_SOLDIER_PER_DAY;
	}

	/** Hoeveel soldaten er echt te zien zijn in de wereld. */
	public static int visible(int soldiers) {
		if (soldiers < 0) throw new IllegalArgumentException("negatief aantal: " + soldiers);
		return Math.min(soldiers, MAX_VISIBLE);
	}

	/** Sterkte = soldaten x uitrusting; bij te weinig eten vecht maar het gevoede deel mee. */
	public static int strength(int soldiers, Gear gear, int foodAvailable) {
		if (soldiers < 0 || foodAvailable < 0) throw new IllegalArgumentException("negatief aantal");
		int fed = Math.min(soldiers, foodAvailable / FOOD_PER_SOLDIER_PER_DAY);
		return fed * gear.strength();
	}
}
