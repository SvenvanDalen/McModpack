package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LairBlueprintTest {
	@Test
	void nestHeeftKistMagmaEnDraakplek() {
		var l = Blueprints.lair();
		assertEquals(1, l.count("chest"));
		assertEquals(25, l.count("magma_block"));
		assertTrue(l.spots().stream().anyMatch(s -> s.role().equals("draak")));
	}

	@Test
	void ingangIsOpenEnBlijftBinnenVolume() {
		var l = Blueprints.lair();
		var laatste = l.placements().stream().filter(p -> p.x() == 12 && p.y() == 2 && p.z() == 2).reduce((a, b) -> b).orElseThrow();
		assertEquals("air", laatste.block());
		for (var p : l.placements()) assertTrue(p.x() >= 0 && p.x() <= 24 && p.z() >= 0 && p.z() <= 24 && p.y() >= -1 && p.y() <= 8, p.toString());
	}
}
