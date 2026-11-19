package com.drakenrijken.core;

/**
 * Gevangenen laten werken (DR-1109): in de mijn, op het land en op schepen.
 * Gevangenen bewaken kost soldaten; hoe harder ze werken, hoe groter de onrust (zie {@code Unrest}).
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class Captives {
	public enum Work {
		MINE(3), FIELD(2), SHIP(2);

		private final int output;

		Work(int output) { this.output = output; }

		/** Opbrengst per gevangene per dag. */
		public int output() { return output; }
	}

	/** Eén bewaker per zoveel gevangenen. */
	public static final int CAPTIVES_PER_GUARD = 5;
	public static final int MAX_CAPTIVES = 100;

	private Captives() {}

	public static int guardsNeeded(int captives) {
		if (captives < 0) throw new IllegalArgumentException("negatief aantal: " + captives);
		return (captives + CAPTIVES_PER_GUARD - 1) / CAPTIVES_PER_GUARD;
	}

	/** Opbrengst per dag; zonder genoeg bewakers werkt alleen het bewaakte deel. */
	public static int dailyOutput(Work work, int captives, int guards) {
		if (captives < 0 || guards < 0) throw new IllegalArgumentException("negatief aantal");
		int working = Math.min(Math.min(captives, MAX_CAPTIVES), guards * CAPTIVES_PER_GUARD);
		return working * work.output();
	}
}
