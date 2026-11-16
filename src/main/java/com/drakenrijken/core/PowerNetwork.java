package com.drakenrijken.core;

import java.util.List;

/**
 * Krachtoverdracht tijdperk 1: bronnen (bijv. waterraden) delen hun kracht via assen en tandwielen.
 * Elke 4 assen kosten 1 kracht aan wrijving; een netwerk heeft hooguit MAX_SHAFTS assen. Waarden zijn eerste inschattingen.
 */
public final class PowerNetwork {
	public static final int MAX_SHAFTS = 32;
	public static final int SHAFTS_PER_LOSS = 4;

	private final int produced;
	private final int shafts;

	public PowerNetwork(List<Integer> sources, int shafts) {
		if (shafts < 0 || shafts > MAX_SHAFTS) {
			throw new IllegalArgumentException("assen buiten 0.." + MAX_SHAFTS + ": " + shafts);
		}
		long sum = 0;
		for (int s : sources) {
			if (s < 0) throw new IllegalArgumentException("kracht is negatief: " + s);
			sum += s;
		}
		this.produced = (int) Math.min(sum, Integer.MAX_VALUE);
		this.shafts = shafts;
	}

	/** Kracht die overblijft voor machines, nooit onder nul. */
	public int available() {
		return Math.max(0, produced - shafts / SHAFTS_PER_LOSS);
	}

	/** Of de machines samen genoeg kracht hebben; vraag is de som van hun benodigde kracht. */
	public boolean canSupply(int demand) {
		if (demand < 0) throw new IllegalArgumentException("vraag is negatief: " + demand);
		return available() >= demand;
	}
}
