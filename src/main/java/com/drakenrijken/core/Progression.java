package com.drakenrijken.core;

public final class Progression {
	public static final int MAX_LEVEL = 30;

	private Progression() {}

	/** Xp die nodig is om van level naar level + 1 te gaan. */
	public static int xpForLevel(int level) {
		if (level < 1) {
			throw new IllegalArgumentException("level moet minstens 1 zijn: " + level);
		}
		return 100 * level;
	}

	/** Voegt xp toe; negatieve of nul-waarden doen niets; bij max level blijft xp 0. */
	public static void addXp(PlayerProgress p, int amount) {
		if (amount <= 0 || p.level() >= MAX_LEVEL) {
			return;
		}
		int level = p.level();
		long xp = (long) p.xp() + amount;
		while (level < MAX_LEVEL && xp >= xpForLevel(level)) {
			xp -= xpForLevel(level);
			level++;
		}
		p.set(level, level == MAX_LEVEL ? 0 : (int) xp);
	}
}
