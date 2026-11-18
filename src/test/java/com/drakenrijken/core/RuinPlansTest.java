package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RuinPlansTest {
	@Test
	void elkePlanPastEnHeeftEenSteenEnEenKist() {
		for (String s : RuinPlans.STORIES) {
			Blueprint p = RuinPlans.plan(s);
			assertTrue(RuinStory.fits(p), s + " past niet in 12x12");
			assertEquals(1, p.count(RuinPlans.STONE), s + " steen");
			assertEquals(1, p.count("chest"), s + " kist");
			assertTrue(p.placements().size() < 600, s + " te groot");
		}
	}

	@Test
	void kapelHeeftEchtWater() {
		assertTrue(RuinPlans.plan("verzonken_kapel").count("water") > 5);
	}

	@Test
	void onbekendVerhaalMagNiet() {
		assertThrows(IllegalArgumentException.class, () -> RuinPlans.plan("x"));
	}
}
