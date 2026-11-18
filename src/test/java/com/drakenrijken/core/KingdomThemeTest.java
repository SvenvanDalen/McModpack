package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KingdomThemeTest {
	/** Aantal koninkrijken in 100x100 chunks bij seed 12345, gemeten vóór de introductie van KingdomTheme. */
	private static final int KONINKRIJKEN_VOOR_WIJZIGING = 119;

	@Test
	void zelfdeSeedGeeftZelfdeThema() {
		for (int i = -50; i < 50; i++) {
			assertEquals(KingdomTheme.of(42L, i, 7 - i), KingdomTheme.of(42L, i, 7 - i));
		}
	}

	@Test
	void ongeveerEenOpVierIsJapans() {
		int koninkrijken = 0, japans = 0;
		for (int x = 0; x < 1000 && koninkrijken < 4000; x++) {
			for (int z = 0; z < 1000 && koninkrijken < 4000; z++) {
				if (!KingdomPlacement.hasKingdom(777L, x, z)) continue;
				koninkrijken++;
				if (KingdomTheme.of(777L, x, z) == KingdomTheme.JAPANS) japans++;
			}
		}
		assertEquals(4000, koninkrijken);
		double aandeel = japans / (double) koninkrijken;
		assertTrue(aandeel > 0.15 && aandeel < 0.35, "aandeel Japans: " + aandeel);
	}

	@Test
	void japansKoninkrijkNooitGroteStad() {
		int japans = 0;
		for (int x = 0; x < 300; x++) {
			for (int z = 0; z < 300; z++) {
				if (KingdomTheme.of(5L, x, z) == KingdomTheme.JAPANS) {
					assertFalse(KingdomTheme.usesCity(5L, x, z));
					japans++;
				} else {
					assertEquals(CityPlans.isCity(5L, x, z), KingdomTheme.usesCity(5L, x, z));
				}
			}
		}
		assertTrue(japans > 0);
	}

	@Test
	void bestaandeKoninkrijkenVerschuivenNiet() {
		int n = 0;
		for (int x = 0; x < 100; x++) {
			for (int z = 0; z < 100; z++) {
				if (KingdomPlacement.hasKingdom(12345L, x, z)) n++;
			}
		}
		assertTrue(n >= 50 && n <= 200);
		assertEquals(KONINKRIJKEN_VOOR_WIJZIGING, n);
	}
}
