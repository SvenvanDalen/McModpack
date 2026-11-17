package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SeasonTest {
	@Test
	void cyclusVan28DagenInVierBlokkenVanZeven() {
		assertEquals(Season.LENTE, Season.of(0));
		assertEquals(Season.LENTE, Season.of(6));
		assertEquals(Season.ZOMER, Season.of(7));
		assertEquals(Season.HERFST, Season.of(14));
		assertEquals(Season.WINTER, Season.of(27));
		assertEquals(Season.LENTE, Season.of(28));
	}

	@Test
	void negatieveDagTeltAlsDagNul() {
		assertEquals(Season.LENTE, Season.of(-5));
		assertEquals(7, Season.daysLeft(-5));
	}

	@Test
	void dagenOverTeltAf() {
		assertEquals(7, Season.daysLeft(0));
		assertEquals(1, Season.daysLeft(6));
		assertEquals(7, Season.daysLeft(7));
	}

	@Test
	void winterIsDeLaagsteOogst() {
		for (Season s : Season.values()) assertTrue(s == Season.WINTER || s.harvestPercent() > Season.WINTER.harvestPercent());
	}
}
