package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BossPhasesTest {
	@Test
	void faseVolgtGezondheid() {
		assertEquals(1, BossPhases.phaseFor(1.0));
		assertEquals(1, BossPhases.phaseFor(0.7));
		assertEquals(2, BossPhases.phaseFor(0.5));
		assertEquals(3, BossPhases.phaseFor(0.2));
		assertEquals(3, BossPhases.phaseFor(0.0));
	}

	@Test
	void ongeldigeVerhoudingWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> BossPhases.phaseFor(-0.1));
		assertThrows(IllegalArgumentException.class, () -> BossPhases.phaseFor(1.1));
		assertThrows(IllegalArgumentException.class, () -> BossPhases.phaseFor(Double.NaN));
	}

	@Test
	void beloningAlleenEenmaalPerSpeler() {
		BossRewards r = new BossRewards();
		assertTrue(r.rewardOnce("a"));
		assertFalse(r.rewardOnce("a"));
		assertTrue(r.rewardOnce("b"));
	}
}
