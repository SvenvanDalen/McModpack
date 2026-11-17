package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BlueprintsTest {
	@Test
	void dungeonHeeftSchatkistEnVal() {
		var d = Blueprints.dungeon();
		assertEquals(1, d.count("chest"));
		assertEquals(9, d.count("magma_block"));
	}

	@Test
	void dungeonLiggenInEenBeperktVolume() {
		for (var p : Blueprints.dungeon().placements()) {
			assertTrue(p.x() >= 0 && p.x() <= 6 + 18, "x " + p.x());
			assertTrue(p.y() >= 0 && p.y() <= 4);
			assertTrue(p.z() >= 0 && p.z() <= 6);
		}
	}

	@Test
	void kistStaatInDeLaatsteKamer() {
		var kist = Blueprints.dungeon().placements().stream().filter(p -> p.block().equals("chest")).findFirst().orElseThrow();
		assertTrue(kist.x() >= (Blueprints.DUNGEON_ROOMS - 1) * 9 + 1);
	}

	@Test
	void roomIsHolBinnenin() {
		Blueprint b = new Blueprint();
		b.room(0, 0, 0, 4, 4, 4, "stone");
		var last = b.placements().stream().filter(p -> p.x() == 2 && p.y() == 2 && p.z() == 2).reduce((a, c) -> c).orElseThrow();
		assertEquals("air", last.block());
	}
}
