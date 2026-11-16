package com.drakenrijken.core;

/** Harde grens op het aantal tegelijk actieve machines, zodat het spel soepel blijft. */
public final class MachineBudget {
	public static final int MAX_ACTIVE = 64;

	private int active;

	/** Probeert een machine te activeren; false als de grens bereikt is. */
	public boolean tryActivate() {
		if (active >= MAX_ACTIVE) {
			return false;
		}
		active++;
		return true;
	}

	/** Meldt dat een machine stopt; zakt nooit onder nul. */
	public void release() {
		if (active > 0) {
			active--;
		}
	}

	public int active() { return active; }
}
