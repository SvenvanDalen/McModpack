package com.drakenrijken.core;

import java.util.LinkedHashSet;
import java.util.Set;

/** Level, xp en ontgrendelde vaardigheden van een speler. */
public final class PlayerProgress {
	private int level;
	private int xp;
	private final Set<String> unlocked = new LinkedHashSet<>();

	public PlayerProgress() {
		this(1, 0);
	}

	public PlayerProgress(int level, int xp) {
		if (level < 1 || level > Progression.MAX_LEVEL) {
			throw new IllegalArgumentException("level buiten 1.." + Progression.MAX_LEVEL + ": " + level);
		}
		if (xp < 0) {
			throw new IllegalArgumentException("xp is negatief: " + xp);
		}
		this.level = level;
		this.xp = level == Progression.MAX_LEVEL ? 0 : xp;
	}

	public int level() { return level; }

	public int xp() { return xp; }

	/** Wijzigbare set; alleen SkillTree hoort hierin te schrijven. */
	public Set<String> unlocked() { return unlocked; }

	void set(int level, int xp) {
		this.level = level;
		this.xp = xp;
	}
}
