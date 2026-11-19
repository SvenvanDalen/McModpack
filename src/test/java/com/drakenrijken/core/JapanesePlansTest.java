package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JapanesePlansTest {
	@Test
	void dorpHeeftNPCPlekkenMetAlleRollen() {
		var rollen = JapanesePlans.village().spots().stream().map(Blueprint.Spot::role).toList();
		for (String r : new String[] {"smid", "marktkoopman", "dorpeling", "sensei"}) assertTrue(rollen.contains(r), "rol ontbreekt: " + r);
	}

	@Test
	void dorpHeeftBedden() {
		assertTrue(JapanesePlans.village().count("white_bed") >= 6);
	}

	@Test
	void toriiAanwezig() {
		assertTrue(JapanesePlans.village().count("red_concrete") >= 6);
	}

	@Test
	void npcPlekkenStaanInDeLucht() {
		var d = JapanesePlans.village();
		for (var s : d.spots()) {
			var laatste = d.placements().stream().filter(p -> p.x() == s.x() && p.y() == s.y() && p.z() == s.z()).reduce((a, c) -> c);
			assertTrue(laatste.isEmpty() || laatste.get().block().equals("air"), "plek bezet: " + s);
		}
	}

	@Test
	void kasteelHeeftTroonzaalEnKerker() {
		var c = JapanesePlans.castle();
		assertEquals(1, c.count("gold_block"));
		assertEquals(1, c.count("chest"));
		assertTrue(c.count("iron_bars") >= 12);
		var rollen = c.spots().stream().map(Blueprint.Spot::role).toList();
		assertTrue(rollen.contains("koning"));
		assertTrue(rollen.contains("bewaker"));
		assertTrue(c.placements().stream().anyMatch(p -> p.y() == -5));
		for (int y = -4; y <= 0; y++) {
			final int yy = y;
			var laatste = c.placements().stream().filter(p -> p.x() == 8 && p.y() == yy && p.z() == 9).reduce((a, b) -> b).orElseThrow();
			assertEquals("air", laatste.block(), "schacht dicht op y=" + y);
		}
	}

	@Test
	void planBlijftBinnenAfmeting() {
		for (var p : JapanesePlans.village().placements()) {
			assertTrue(p.x() >= 0 && p.x() < JapanesePlans.VILLAGE_SIZE && p.z() >= 0 && p.z() < JapanesePlans.VILLAGE_SIZE && p.y() >= -1, p.toString());
		}
		for (var p : JapanesePlans.castle().placements()) {
			assertTrue(p.x() >= 0 && p.x() <= 20 && p.z() >= 0 && p.z() <= 20 && p.y() >= -5, p.toString());
		}
	}

	@Test
	void aantalPlacementsBlijftBeperkt() {
		int dorp = JapanesePlans.village().placements().size(), burcht = JapanesePlans.castle().placements().size();
		// Het Japanse dorp is 32x32 tegen 30x12 van het westerse (2,7 keer de oppervlakte): maximaal drie keer zoveel plaatsingen.
		assertTrue(dorp < 3 * Blueprints.village().placements().size(), "dorp " + dorp + " vs " + Blueprints.village().placements().size());
		assertTrue(burcht < 2 * Blueprints.castle().placements().size(), "burcht " + burcht + " vs " + Blueprints.castle().placements().size());
	}
}
