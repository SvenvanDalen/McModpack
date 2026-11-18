package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KingdomGrowthTest {
	@Test
	void zelfdeInvoerGeeftZelfdeNiveau() {
		assertEquals(KingdomGrowth.level(1L, 3, 4, 100), KingdomGrowth.level(1L, 3, 4, 100));
	}

	@Test
	void beginniveauLigtTussenTweeEnVier() {
		for (int cx = 0; cx < 50; cx++) {
			int s = KingdomGrowth.startLevel(7L, cx, cx * 3);
			assertTrue(s >= 2 && s <= 4, "start " + s);
		}
	}

	@Test
	void opDagNulIsHetBeginniveau() {
		assertEquals(KingdomGrowth.startLevel(5L, 2, 9), KingdomGrowth.level(5L, 2, 9, 0));
		assertEquals(KingdomGrowth.startLevel(5L, 2, 9), KingdomGrowth.level(5L, 2, 9, -50));
	}

	@Test
	void niveauBlijftAltijdBinnenDeGrenzen() {
		for (int cx = 0; cx < 60; cx++) for (long day : new long[] {0, 19, 20, 400, 100_000}) {
			int l = KingdomGrowth.level(9L, cx, -cx, day);
			assertTrue(l >= KingdomGrowth.RUINED && l <= KingdomGrowth.MAX_LEVEL, "niveau " + l);
		}
	}

	@Test
	void groeiendRijkStijgtEnVervallendRijkDaaltMonotoon() {
		boolean groei = false, verval = false;
		for (int cx = 0; cx < 200; cx++) {
			var trend = KingdomGrowth.trend(3L, cx, 1);
			int prev = KingdomGrowth.level(3L, cx, 1, 0);
			for (long d = 0; d <= 400; d += KingdomGrowth.DAYS_PER_STEP) {
				int l = KingdomGrowth.level(3L, cx, 1, d);
				switch (trend) {
					case GROWING -> assertTrue(l >= prev);
					case DECLINING -> assertTrue(l <= prev);
					case STABLE -> assertEquals(prev, l);
				}
				prev = l;
			}
			if (trend == KingdomGrowth.Trend.GROWING) { groei = true; assertEquals(KingdomGrowth.MAX_LEVEL, prev); }
			if (trend == KingdomGrowth.Trend.DECLINING) { verval = true; assertTrue(KingdomGrowth.isRuined(3L, cx, 1, 400)); }
		}
		assertTrue(groei && verval);
	}

	@Test
	void tweehonderdInwonersBijHoogsteNiveau() {
		assertEquals(200, KingdomGrowth.inhabitants(KingdomGrowth.MAX_LEVEL));
		assertEquals(0, KingdomGrowth.inhabitants(-3));
		assertEquals(200, KingdomGrowth.inhabitants(99));
	}
}
