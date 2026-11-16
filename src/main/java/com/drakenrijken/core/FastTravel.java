package com.drakenrijken.core;

import java.util.LinkedHashMap;
import java.util.Map;

/** Snelreis tussen ontdekte dorpen tegen betaling. Prijs en afstand zijn eerste inschattingen. */
public final class FastTravel {
	public enum Result { OK, UNKNOWN_VILLAGE, NOT_DISCOVERED, SAME_PLACE, TOO_POOR }

	public static final int BLOCKS_PER_COIN = 100;
	public static final int MIN_COST = 5;

	public record Village(String id, int x, int z) {}

	private final Map<String, Village> villages = new LinkedHashMap<>();

	public void register(Village v) {
		if (villages.putIfAbsent(v.id(), v) != null) throw new IllegalArgumentException("dubbel dorp: " + v.id());
	}

	/** Prijs voor de reis: afstand (Manhattan) per 100 blokken een munt, minimaal 5. */
	public int cost(String from, String to) {
		Village a = villages.get(from);
		Village b = villages.get(to);
		if (a == null || b == null) throw new IllegalArgumentException("onbekend dorp");
		long dist = Math.abs((long) a.x() - b.x()) + Math.abs((long) a.z() - b.z());
		return (int) Math.max(MIN_COST, Math.min(Integer.MAX_VALUE, dist / BLOCKS_PER_COIN));
	}

	/** Controleert de reis; bij OK is de prijs af te trekken van de munten (cost(from, to)). */
	public Result check(java.util.Set<String> discovered, String from, String to, int coins) {
		if (!villages.containsKey(from) || !villages.containsKey(to)) return Result.UNKNOWN_VILLAGE;
		if (from.equals(to)) return Result.SAME_PLACE;
		if (!discovered.contains(from) || !discovered.contains(to)) return Result.NOT_DISCOVERED;
		return coins < cost(from, to) ? Result.TOO_POOR : Result.OK;
	}
}
