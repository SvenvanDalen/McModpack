package com.drakenrijken.core;

/** Stadsmuur (#153): hoe meer muur rond het rijk, hoe kleiner een aanval. Waarden zijn eerste inschattingen om te balanceren. */
public final class WallStrength {
	/** Minder muurblokken dan dit telt niet mee. */
	public static final int MIN_BLOCKS = 20;
	/** Een muur haalt hoogstens dit percentage van de golf af; nooit alles. */
	public static final int MAX_REDUCTION_PERCENT = 30;

	private WallStrength() {}

	/** 0 onder 20 blokken, daarna 1 procent per 4 blokken, hoogstens 30 (bij 120 blokken). */
	public static int reductionPercent(int wallBlocks) {
		if (wallBlocks < 0) throw new IllegalArgumentException("aantal muurblokken is negatief: " + wallBlocks);
		if (wallBlocks < MIN_BLOCKS) return 0;
		return Math.min(MAX_REDUCTION_PERCENT, wallBlocks / 4);
	}

	/** Golfgrootte na de muur: nooit onder 1 zolang er een golf is. */
	public static int apply(int waveSize, int wallBlocks) {
		if (waveSize < 0) throw new IllegalArgumentException("golfgrootte is negatief: " + waveSize);
		if (waveSize == 0) return 0;
		return Math.max(1, waveSize - waveSize * reductionPercent(wallBlocks) / 100);
	}
}
