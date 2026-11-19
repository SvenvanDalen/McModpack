package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JapaneseLandTest {
	@Test
	void tuinHeeftKersenhoutEnBamboe() {
		var g = JapaneseLand.garden();
		assertTrue(g.count("cherry_log") >= 4);
		assertTrue(g.count("bamboo") >= 10);
		assertTrue(g.count("water") >= 1);
		assertTrue(g.count("stone") >= 1);
	}

	@Test
	void tuinBlijftBinnenAfmeting() {
		for (var p : JapaneseLand.garden().placements()) {
			assertTrue(p.x() >= 0 && p.x() < JapaneseLand.SIZE && p.z() >= 0 && p.z() < JapaneseLand.SIZE && p.y() >= -1, p.toString());
		}
	}

	@Test
	void aantalBlokkenBinnenBudget() {
		assertTrue(JapaneseLand.garden().placements().size() <= 20000);
	}
}
