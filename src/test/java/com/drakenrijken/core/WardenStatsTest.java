package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WardenStatsTest {
	@Test
	void schokgolvenKomenSnellerEnHarder() {
		for (int f = 1; f < 3; f++) {
			assertTrue(WardenStats.shockInterval(f + 1) < WardenStats.shockInterval(f));
			assertTrue(WardenStats.damageFactor(f + 1) > WardenStats.damageFactor(f));
		}
	}

	@Test
	void ongeldigeFase() {
		assertThrows(IllegalArgumentException.class, () -> WardenStats.shockInterval(0));
		assertThrows(IllegalArgumentException.class, () -> WardenStats.damageFactor(4));
	}
}
