package com.drakenrijken.core;

/** Telt geplaatste blokken en geeft xp bij elk vol tiental. */
public final class BuildCounter {
	private int pending;

	/** Registreert een geplaatst blok; geeft de xp die daarbij hoort (meestal 0). */
	public int place() {
		pending++;
		if (pending >= XpRules.BLOCKS_PER_XP) {
			pending -= XpRules.BLOCKS_PER_XP;
			return 1;
		}
		return 0;
	}

	public int pending() { return pending; }
}
