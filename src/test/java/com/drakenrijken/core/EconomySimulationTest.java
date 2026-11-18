package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Simulatie van 90 speldagen (voorstel 7): goudinkomen van belasting en beroepen tegen de gouddump. */
class EconomySimulationTest {
	static final int DAYS = 90;
	static final int BASIC_ITEM_GOLD = 20;
	static final int WALL_BLOCKS = 200;

	/** Inkomen na onderhoud van een muur, met inning elke 5 dagen (het plafond). */
	static int net(int level) {
		int total = 0;
		long last = 0;
		for (long d = 1; d <= DAYS; d++) {
			if (d % RealmTax.MAX_DAYS == 0) {
				total += RealmTax.owed(level, last, d) + Profession.payout(level, last, d);
				last = d;
			}
			total -= GoldSinks.wallUpkeepPerDay(WALL_BLOCKS);
		}
		return total;
	}

	@Test
	void hogerNiveauVerdientMeer() {
		for (int l = 1; l <= RealmState.MAX_LEVEL; l++) assertTrue(net(l) >= net(l - 1), "niveau " + l);
	}

	@Test
	void muurOnderhoudVreetNietAllesOpOpHetTopniveau() {
		int top = net(RealmState.MAX_LEVEL);
		assertTrue(top > 0);
		assertTrue(top < DAYS * 100, "geen onbeperkte goudstroom: " + top);
	}

	@Test
	void basisitemIsGeenWegwerpgeld() {
		// Op het topniveau kost een basisitem minstens een halve speldag aan inkomen.
		int perDag = net(RealmState.MAX_LEVEL) / DAYS;
		assertTrue(BASIC_ITEM_GOLD >= perDag / 2, "dag-inkomen " + perDag);
	}

	@Test
	void prijsSchommelingBlijftBinnenGrenzen() {
		for (long d = 0; d < DAYS; d++) {
			for (int l = 0; l <= RealmState.MAX_LEVEL; l++) {
				int p = MarketPrice.totalPercent(l, d);
				assertTrue(p >= MarketPrice.FLOOR_PERCENT && p <= MarketPrice.CEIL_PERCENT);
			}
		}
	}
}
