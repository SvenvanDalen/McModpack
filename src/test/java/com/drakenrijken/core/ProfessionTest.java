package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProfessionTest {
	@Test
	void verdelingTeltOpTotHetAantal() {
		for (int n = 0; n <= 30; n++) {
			int sum = 0;
			for (int x : Profession.assign(n)) sum += x;
			assertEquals(n, sum);
		}
	}

	@Test
	void restGaatNaarDeBoer() {
		assertEquals(3, Profession.assign(6)[0]);
		assertEquals(1, Profession.assign(6)[1]);
	}

	@Test
	void negatiefAantalGeeftNul() {
		assertEquals(0, Profession.assign(-3)[0]);
	}

	@Test
	void opbrengstGroeitMetHetNiveauEnBlijftBegrensd() {
		for (int l = 1; l <= RealmState.MAX_LEVEL; l++) assertTrue(Profession.dailyYield(l) >= Profession.dailyYield(l - 1));
		assertTrue(Profession.dailyYield(RealmState.MAX_LEVEL) <= EntityBudget.MAX_NPCS * 2);
	}

	@Test
	void seizoenWerktAlleenOpDeBoeren() {
		int l = RealmState.MAX_LEVEL;
		int boeren = Profession.assign(RealmVillagers.target(l))[Profession.BOER.ordinal()];
		assertEquals(Profession.dailyYield(l), Profession.dailyYield(l, Season.LENTE));
		int vol = boeren * Profession.BOER.dailyYield();
		assertEquals(Profession.dailyYield(l) - vol + vol * Season.WINTER.harvestPercent() / 100, Profession.dailyYield(l, Season.WINTER));
		assertTrue(Profession.dailyYield(l, Season.ZOMER) >= Profession.dailyYield(l, Season.HERFST));
	}
}
