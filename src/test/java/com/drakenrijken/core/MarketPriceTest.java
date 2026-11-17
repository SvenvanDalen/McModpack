package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarketPriceTest {
	@Test
	void dagfactorBlijftInDeGrenzen() {
		boolean boven = false, onder = false;
		for (long d = 0; d < 1000; d++) {
			int p = MarketPrice.dayPercent(d);
			assertTrue(p >= MarketPrice.MIN_PERCENT && p <= MarketPrice.MAX_PERCENT, "dag " + d);
			boven |= p > 100;
			onder |= p < 100;
		}
		assertTrue(boven && onder, "prijs moet echt schommelen");
	}

	@Test
	void zelfdeDagGeeftZelfdeFactor() {
		assertEquals(MarketPrice.dayPercent(7), MarketPrice.dayPercent(7));
	}

	@Test
	void totaalpercentageBlijftTussenVijftigEnHonderdvijftig() {
		for (int l = 0; l <= RealmState.MAX_LEVEL; l++) {
			for (long d = 0; d < 200; d++) {
				int p = MarketPrice.totalPercent(l, d);
				assertTrue(p >= MarketPrice.FLOOR_PERCENT && p <= MarketPrice.CEIL_PERCENT);
			}
		}
	}

	@Test
	void hogerRijkIsAltijdGoedkoperOpDezelfdeDag() {
		for (long d = 0; d < 50; d++) assertTrue(MarketPrice.totalPercent(5, d) <= MarketPrice.totalPercent(0, d));
	}

	@Test
	void welwillendheidVerschuiftHoogstensDrie() {
		for (long d = 0; d < 200; d++) assertTrue(Math.abs(MarketPrice.goodwillShift(d)) <= 3);
	}

	@Test
	void negatieveDagWordtGeweigerd() {
		assertThrows(IllegalArgumentException.class, () -> MarketPrice.dayPercent(-1));
	}
}
