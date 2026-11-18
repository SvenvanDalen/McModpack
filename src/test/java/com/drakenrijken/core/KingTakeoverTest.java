package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KingTakeoverTest {
	@Test
	void omkopenKostMeerBijGroterKoninkrijk() {
		assertEquals(200, KingTakeover.bribeCost(1));
		assertTrue(KingTakeover.bribeCost(4) > KingTakeover.bribeCost(2));
		assertTrue(KingTakeover.canBribe(2, 400));
		assertFalse(KingTakeover.canBribe(2, 399));
	}

	@Test
	void afzettenVraagtSterkerLegerDanDeWacht() {
		assertEquals(120, KingTakeover.guardStrength(3));
		assertFalse(KingTakeover.canDepose(3, 120));
		assertTrue(KingTakeover.canDepose(3, 121));
		assertTrue(KingTakeover.canDepose(2, Army.strength(30, Army.Gear.IRON, 30)));
	}

	@Test
	void troonInnemenVraagtEigenRijk() {
		assertFalse(KingTakeover.canTakeThrone(4, 3));
		assertTrue(KingTakeover.canTakeThrone(4, 4));
	}

	@Test
	void ongeldigeInvoerIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> KingTakeover.bribeCost(0));
		assertThrows(IllegalArgumentException.class, () -> KingTakeover.guardStrength(6));
		assertThrows(IllegalArgumentException.class, () -> KingTakeover.canBribe(1, -1));
		assertThrows(IllegalArgumentException.class, () -> KingTakeover.canDepose(1, -1));
	}
}
