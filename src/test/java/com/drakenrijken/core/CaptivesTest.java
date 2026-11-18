package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CaptivesTest {
	@Test
	void ElkeVijfGevangenenVraagtEenBewaker() {
		assertEquals(0, Captives.guardsNeeded(0));
		assertEquals(1, Captives.guardsNeeded(5));
		assertEquals(2, Captives.guardsNeeded(6));
	}

	@Test
	void mijnLevertMeerOpDanLandEnSchip() {
		assertTrue(Captives.dailyOutput(Captives.Work.MINE, 10, 2) > Captives.dailyOutput(Captives.Work.FIELD, 10, 2));
		assertEquals(20, Captives.dailyOutput(Captives.Work.SHIP, 10, 2));
	}

	@Test
	void ZonderGenoegBewakersWerktAlleenHetBewaakteDeel() {
		assertEquals(15, Captives.dailyOutput(Captives.Work.MINE, 10, 1) * 1); // 5 bewaakt x 3
		assertEquals(0, Captives.dailyOutput(Captives.Work.MINE, 10, 0));
	}

	@Test
	void MaximumAantalGevangenenWerkt() {
		assertEquals(Captives.MAX_CAPTIVES * 2, Captives.dailyOutput(Captives.Work.FIELD, 500, 200));
	}

	@Test
	void negatiefIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> Captives.guardsNeeded(-1));
		assertThrows(IllegalArgumentException.class, () -> Captives.dailyOutput(Captives.Work.MINE, -1, 0));
	}
}
