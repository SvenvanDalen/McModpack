package com.drakenrijken.core;

import java.util.Map;

/**
 * Erkenning door een koning (DR-1102): een koning erkent je pas als je zelf bouwwerken hebt neergezet.
 * Hoe groter het koninkrijk (zie {@link KingdomGrowth}), hoe meer soorten bouwwerk hij eist.
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class Recognition {
	/** Soorten bouwwerk, in de volgorde waarin een koning ze eist, met het minimum aantal blokken. */
	public enum Kind {
		TROONZAAL(120), HAVEN(150), KAZERNE(200);

		private final int minBlocks;

		Kind(int minBlocks) { this.minBlocks = minBlocks; }

		public int minBlocks() { return minBlocks; }
	}

	private Recognition() {}

	/** Hoeveel soorten bouwwerk een koning van dit niveau eist: 0 voor verwoeste rijken, 1 tot niveau 2, 2 tot niveau 4, anders 3. */
	public static int required(int kingdomLevel) {
		if (kingdomLevel <= KingdomGrowth.RUINED) return 0;
		if (kingdomLevel <= 2) return 1;
		if (kingdomLevel <= 4) return 2;
		return Kind.values().length;
	}

	/** Welke soorten bouwwerk de speler heeft neergezet (genoeg blokken per soort). */
	public static int built(Map<Kind, Integer> blocks) {
		int n = 0;
		for (Kind k : Kind.values()) if (blocks.getOrDefault(k, 0) >= k.minBlocks()) n++;
		return n;
	}

	public static boolean recognised(int kingdomLevel, Map<Kind, Integer> blocks) {
		return built(blocks) >= required(kingdomLevel);
	}

	/** De eerstvolgende soort die nog ontbreekt, of null als er niets meer ontbreekt. */
	public static Kind missing(int kingdomLevel, Map<Kind, Integer> blocks) {
		if (recognised(kingdomLevel, blocks)) return null;
		for (Kind k : Kind.values()) if (blocks.getOrDefault(k, 0) < k.minBlocks()) return k;
		return null;
	}
}
