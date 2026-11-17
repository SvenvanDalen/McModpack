package com.drakenrijken.core;

import java.util.Set;

/** Stoomtrein tussen twee dorpen (#144): hergebruikt de prijs van {@link FastTravel} met korting en vraagt kracht van de ketel. Waarden zijn eerste inschattingen. */
public final class TrainLine {
	public enum Result { OK, UNKNOWN_VILLAGE, NOT_DISCOVERED, SAME_PLACE, TOO_POOR, NO_POWER }

	public static final int SPEED_BLOCKS_PER_SECOND = 8;
	public static final int DISCOUNT_PERCENT = 50;
	public static final int MIN_POWER = SteamBoiler.POWER;

	private TrainLine() {}

	/** Prijs van de trein: de snelreisprijs met korting, minimaal 1. */
	public static int price(FastTravel travel, String from, String to) {
		return Math.max(1, travel.cost(from, to) * (100 - DISCOUNT_PERCENT) / 100);
	}

	/** Reistijd in seconden voor een afstand in blokken, naar boven afgerond; nooit negatief. */
	public static int travelSeconds(long distanceBlocks) {
		long d = Math.max(0, distanceBlocks);
		return (int) Math.min(Integer.MAX_VALUE, (d + SPEED_BLOCKS_PER_SECOND - 1) / SPEED_BLOCKS_PER_SECOND);
	}

	/** Controleert de rit: eerst de snelreisregels (ontdekt, bekend, anders dan het beginpunt), dan de kracht, dan het geld. */
	public static Result check(FastTravel travel, Set<String> discovered, String from, String to, int coins, int boilerPower) {
		FastTravel.Result r = travel.check(discovered, from, to, Integer.MAX_VALUE);
		if (r != FastTravel.Result.OK) return Result.valueOf(r.name());
		if (boilerPower < MIN_POWER) return Result.NO_POWER;
		return coins < price(travel, from, to) ? Result.TOO_POOR : Result.OK;
	}
}
