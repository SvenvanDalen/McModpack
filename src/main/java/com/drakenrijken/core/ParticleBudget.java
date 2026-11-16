package com.drakenrijken.core;

/** Grens op eigen deeltjes per tick, uit te zetten via de config (DR-804). */
public final class ParticleBudget {
	public static final int DEFAULT_MAX_PER_TICK = 32;

	private final boolean enabled;
	private final int max;
	private int used;

	public ParticleBudget(boolean enabled, int maxPerTick) {
		if (maxPerTick < 0) throw new IllegalArgumentException("maxPerTick < 0");
		this.enabled = enabled;
		this.max = maxPerTick;
	}

	/** Vraagt om {@code wanted} deeltjes en geeft terug hoeveel er mogen; 0 als uitgezet. */
	public int request(int wanted) {
		if (!enabled || wanted <= 0) return 0;
		int granted = Math.min(wanted, max - used);
		used += granted;
		return granted;
	}

	/** Begin van een nieuwe tick. */
	public void nextTick() { used = 0; }
}
