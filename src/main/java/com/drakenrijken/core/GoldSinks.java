package com.drakenrijken.core;

/** Gouddump (#153, #146): bouwplannen kosten goud en een stadsmuur vraagt onderhoud. Waarden zijn voorstellen uit de economiecheck. */
public final class GoldSinks {
	public static final int BLOCKS_PER_GOLD_PLAN = 10;
	public static final int WALL_BLOCKS_PER_GOLD_PER_DAY = 20;

	private GoldSinks() {}

	/** Goud voor een bouwplan: 1 per 10 blokken, naar boven afgerond; een leeg plan is gratis. */
	public static int blueprintGold(Blueprint plan) {
		int blokken = BlueprintCost.materials(plan).values().stream().mapToInt(Integer::intValue).sum();
		return (blokken + BLOCKS_PER_GOLD_PLAN - 1) / BLOCKS_PER_GOLD_PLAN;
	}

	/** Onderhoud van een muur per speldag: 1 goud per 20 blokken, naar boven afgerond. */
	public static int wallUpkeepPerDay(int wallBlocks) {
		int n = Math.max(0, wallBlocks);
		return (n + WALL_BLOCKS_PER_GOLD_PER_DAY - 1) / WALL_BLOCKS_PER_GOLD_PER_DAY;
	}
}
