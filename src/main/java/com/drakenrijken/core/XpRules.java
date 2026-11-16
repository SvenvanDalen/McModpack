package com.drakenrijken.core;

/** Hoeveel xp elke bron geeft. */
public final class XpRules {
	public static final int BLOCKS_PER_XP = 10;

	private XpRules() {}

	/** Een mob geeft de helft van zijn gezondheid aan xp, minstens 1. */
	public static int mobKill(double maxHealth) {
		return Math.max(1, (int) Math.round(maxHealth / 2));
	}

	/** Een xp per 10 geplaatste blokken. */
	public static int buildXp(int blocksPlaced) {
		return Math.max(0, blocksPlaced) / BLOCKS_PER_XP;
	}

	public static void award(PlayerProgress p, int xp) {
		Progression.addXp(p, xp);
	}
}
