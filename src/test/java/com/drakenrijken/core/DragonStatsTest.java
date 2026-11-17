package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DragonStatsTest {
	@Test
	void fasenWordenSterker() {
		for (int f = 1; f < 3; f++) {
			assertTrue(DragonStats.speedFactor(f + 1) > DragonStats.speedFactor(f));
			assertTrue(DragonStats.damageFactor(f + 1) > DragonStats.damageFactor(f));
		}
		assertEquals(1.0, DragonStats.speedFactor(1));
	}

	@Test
	void vuurVanafFaseTwee() {
		assertFalse(DragonStats.breathesFire(1));
		assertTrue(DragonStats.breathesFire(2));
		assertTrue(DragonStats.breathesFire(3));
	}

	@Test
	void ongeldigeFase() {
		assertThrows(IllegalArgumentException.class, () -> DragonStats.speedFactor(0));
		assertThrows(IllegalArgumentException.class, () -> DragonStats.damageFactor(4));
	}

	@Test
	void vuurCyclusWordtSneller() {
		assertEquals(100, DragonStats.fireInterval(2));
		assertTrue(DragonStats.fireInterval(3) < DragonStats.fireInterval(2));
		assertThrows(IllegalArgumentException.class, () -> DragonStats.fireInterval(0));
	}
}
