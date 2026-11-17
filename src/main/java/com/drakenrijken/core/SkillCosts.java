package com.drakenrijken.core;

import java.util.Map;

/** Wat een actieve vaardigheid kost aan mana of uithouding (eerste inschatting, nog te balanceren). */
public final class SkillCosts {
	private static final Map<String, Integer> COSTS = Map.of("vuurbal", 20);

	private SkillCosts() {}

	/** Kosten van een actieve vaardigheid, of -1 als het geen actieve vaardigheid is. */
	public static int costOf(String id) { return COSTS.getOrDefault(id, -1); }

	public enum Result { OK, PASSIVE, NOT_UNLOCKED, TOO_TIRED }

	/** Probeert de vaardigheid te gebruiken; alleen bij OK is de balk verlaagd. */
	public static Result use(PlayerProgress p, ResourceBar bar, String id) {
		int cost = costOf(id);
		if (cost < 0) return Result.PASSIVE;
		if (!p.unlocked().contains(id)) return Result.NOT_UNLOCKED;
		return bar.spend(cost) ? Result.OK : Result.TOO_TIRED;
	}
}
