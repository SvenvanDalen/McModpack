package com.drakenrijken.core;

import java.util.HashSet;
import java.util.Set;

/** Houdt bij welke spelers de baasbeloning al kregen; elke speler krijgt haar maar één keer. */
public final class BossRewards {
	private final Set<String> rewarded = new HashSet<>();

	/** True alleen bij de eerste keer voor deze speler. */
	public boolean rewardOnce(String playerId) {
		return rewarded.add(playerId);
	}
}
