package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VillageBlueprintTest {
	@Test
	void dorpHeeftHuizenMarktEnSmid() {
		var d = Blueprints.village();
		assertEquals(3, d.count("white_bed"));
		assertEquals(2, d.count("hay_block"));
		assertEquals(1, d.count("anvil"));
		assertEquals(1, d.count("furnace"));
	}

	@Test
	void npcPlekkenVoorElkeRol() {
		var rollen = Blueprints.village().spots().stream().map(Blueprint.Spot::role).toList();
		assertEquals(3, rollen.stream().filter("dorpeling"::equals).count());
		assertTrue(rollen.contains("marktkoopman"));
		assertTrue(rollen.contains("smid"));
	}

	@Test
	void npcPlekkenStaanInDeLucht() {
		var d = Blueprints.village();
		for (var s : d.spots()) {
			var laatste = d.placements().stream().filter(p -> p.x() == s.x() && p.y() == s.y() && p.z() == s.z()).reduce((a, c) -> c);
			assertTrue(laatste.isEmpty() || laatste.get().block().equals("air"), "plek bezet: " + s);
		}
	}

	@Test
	void dorpBlijftBinnenDertigBijTwaalf() {
		for (var p : Blueprints.village().placements()) {
			assertTrue(p.x() >= 0 && p.x() < 30 && p.z() >= 0 && p.z() < 12 && p.y() >= -1 && p.y() <= 4, p.toString());
		}
	}
}
