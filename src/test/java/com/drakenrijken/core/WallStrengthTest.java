package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WallStrengthTest {
	@Test
	void kleineMuurTeltNietMee() {
		assertEquals(0, WallStrength.reductionPercent(0));
		assertEquals(0, WallStrength.reductionPercent(WallStrength.MIN_BLOCKS - 1));
		assertEquals(5, WallStrength.reductionPercent(WallStrength.MIN_BLOCKS));
	}

	@Test
	void verminderingHeeftEenPlafond() {
		assertEquals(30, WallStrength.reductionPercent(120));
		assertEquals(30, WallStrength.reductionPercent(100000));
	}

	@Test
	void golfWordtKleinerMaarNooitNul() {
		assertEquals(9, WallStrength.apply(9, 0));
		assertEquals(7, WallStrength.apply(9, 120));
		for (int l = RealmRaids.FIRST_RAID_LEVEL; l <= RealmState.MAX_LEVEL; l++) {
			assertTrue(WallStrength.apply(RealmRaids.waveSize(l), 100000) >= 1);
		}
		assertEquals(1, WallStrength.apply(1, 100000));
		assertEquals(0, WallStrength.apply(0, 100000));
	}

	@Test
	void ongeldigeInvoerWordtGeweigerd() {
		assertThrows(IllegalArgumentException.class, () -> WallStrength.reductionPercent(-1));
		assertThrows(IllegalArgumentException.class, () -> WallStrength.apply(-1, 5));
	}
}
