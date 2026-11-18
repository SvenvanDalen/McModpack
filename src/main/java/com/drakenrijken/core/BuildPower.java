package com.drakenrijken.core;

import java.util.List;

/**
 * Macht en aanzien door je eigen bouwwerk (DR-1103): hoe meer je zelf bouwt, hoe hoger het rijksniveau.
 * Een hoger niveau trekt meer dorpelingen aan ({@link RealmVillagers}) maar ook meer aanvallen ({@link RealmRaids}).
 * Waarden zijn eerste inschattingen om te balanceren.
 */
public final class BuildPower {
	/** Zoveel blokken gebouwd geven 1 punt macht. */
	public static final int BLOCKS_PER_POINT = 40;
	/** Bouwen alleen brengt je hooguit tot niveau 3; machines zijn nodig voor de hoogste niveaus. */
	public static final int MAX_POINTS = 300;

	private BuildPower() {}

	public static int fromBlocks(int builtBlocks) {
		if (builtBlocks < 0) throw new IllegalArgumentException("aantal blokken is negatief: " + builtBlocks);
		return Math.min(MAX_POINTS, builtBlocks / BLOCKS_PER_POINT);
	}

	/** Totale macht: machines plus bouwwerk. */
	public static int total(List<Integer> machinePower, int builtBlocks) {
		long sum = (long) RealmPower.total(machinePower) + fromBlocks(builtBlocks);
		return (int) Math.min(sum, Integer.MAX_VALUE);
	}

	public static int level(List<Integer> machinePower, int builtBlocks) {
		return RealmState.levelFor(total(machinePower, builtBlocks));
	}
}
