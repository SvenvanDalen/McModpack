package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CastleBlueprintTest {
	@Test
	void burchtHeeftTroonKistEnCellen() {
		var c = Blueprints.castle();
		assertEquals(1, c.count("gold_block"));
		assertEquals(1, c.count("chest"));
		assertTrue(c.count("iron_bars") >= 12);
	}

	@Test
	void koningEnBewakerHebbenPlekken() {
		var rollen = Blueprints.castle().spots().stream().map(Blueprint.Spot::role).toList();
		assertTrue(rollen.contains("koning"));
		assertTrue(rollen.contains("bewaker"));
	}

	@Test
	void kerkerLigtOnderDeZaalEnIsBereikbaar() {
		var c = Blueprints.castle();
		assertTrue(c.placements().stream().anyMatch(p -> p.y() == -5));
		for (int y = -4; y <= 0; y++) {
			final int yy = y;
			var laatste = c.placements().stream().filter(p -> p.x() == 8 && p.y() == yy && p.z() == 9).reduce((a, b) -> b).orElseThrow();
			assertEquals("air", laatste.block(), "schacht dicht op y=" + y);
		}
	}

	@Test
	void burchtBlijftBinnenZijnVolume() {
		for (var p : Blueprints.castle().placements()) {
			assertTrue(p.x() >= 0 && p.x() <= 20 && p.z() >= 0 && p.z() <= 20 && p.y() >= -5 && p.y() <= 9, p.toString());
		}
	}
}
