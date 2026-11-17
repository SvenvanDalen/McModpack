package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KingdomNearTest {
	@Test
	void nearVindtPreciesDeKoninkrijkenInDeStraal() {
		long seed = 12345L;
		for (int cx = -60; cx < 60; cx++) for (int cz = -60; cz < 60; cz++) {
			if (!KingdomPlacement.hasKingdom(seed, cx, cz)) continue;
			int mx = cx * 16 + 8, mz = cz * 16 + 8;
			assertTrue(KingdomPlacement.near(seed, mx + 30, mz - 20, 64).stream().anyMatch(m -> m[0] == mx && m[1] == mz));
			assertTrue(KingdomPlacement.near(seed, mx + 200, mz, 64).stream().noneMatch(m -> m[0] == mx && m[1] == mz));
			return;
		}
		throw new AssertionError("geen koninkrijk gevonden");
	}

	@Test
	void nearGeeftNietsBuitenDeStraal() {
		for (int[] m : KingdomPlacement.near(7L, 0, 0, 500)) {
			assertTrue((long) m[0] * m[0] + (long) m[1] * m[1] <= 500L * 500L);
		}
		assertEquals(0, KingdomPlacement.near(7L, 0, 0, 0).stream().filter(m -> m[0] != 0 || m[1] != 0).count());
	}
}
