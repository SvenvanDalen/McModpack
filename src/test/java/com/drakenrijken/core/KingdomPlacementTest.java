package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KingdomPlacementTest {
	private static int count(long seed, int offsetX, int offsetZ) {
		int n = 0;
		for (int x = 0; x < 100; x++) {
			for (int z = 0; z < 100; z++) {
				if (KingdomPlacement.hasKingdom(seed, x + offsetX, z + offsetZ)) n++;
			}
		}
		return n;
	}

	@Test
	void zelfdeSeedEnChunkGeeftAltijdHetzelfde() {
		for (int i = -50; i < 50; i++) {
			assertEquals(KingdomPlacement.hasKingdom(42L, i, 7 - i), KingdomPlacement.hasKingdom(42L, i, 7 - i));
		}
	}

	@Test
	void dichtheidTussenHalfEnTweeProcentBij10000Chunks() {
		for (long seed : new long[] {0L, 1L, 42L, -987654321L, Long.MAX_VALUE}) {
			int n = count(seed, 0, 0);
			assertTrue(n >= 50 && n <= 200, "seed " + seed + " gaf " + n);
		}
	}

	@Test
	void dichtheidKloptOokVerAanDeRandEnInNegatiefGebied() {
		for (int[] o : new int[][] {{-100, -100}, {1_000_000, -2_000_000}, {-30_000_000, 30_000_000}}) {
			int n = count(7L, o[0], o[1]);
			assertTrue(n >= 50 && n <= 200, "offset " + o[0] + "," + o[1] + " gaf " + n);
		}
	}

	@Test
	void anderZaadGeeftAnderePatroon() {
		int verschil = 0;
		for (int x = 0; x < 300; x++) {
			for (int z = 0; z < 100; z++) {
				if (KingdomPlacement.hasKingdom(1L, x, z) != KingdomPlacement.hasKingdom(2L, x, z)) verschil++;
			}
		}
		assertNotEquals(0, verschil);
	}
}
