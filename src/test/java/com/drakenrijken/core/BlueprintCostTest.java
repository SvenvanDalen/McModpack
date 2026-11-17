package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

class BlueprintCostTest {
	private static Blueprint kamer() {
		Blueprint b = new Blueprint();
		b.room(0, 0, 0, 4, 3, 4, "stone");
		return b;
	}

	@Test
	void luchtKostNiets() {
		var m = BlueprintCost.materials(kamer());
		assertFalse(m.containsKey("air"));
		assertEquals(100 - 3 * 2 * 3, m.get("stone"));
	}

	@Test
	void groottePast16maalNietZeventien() {
		Blueprint ok = new Blueprint();
		ok.set(0, 0, 0, "stone");
		ok.set(15, 15, 15, "stone");
		assertTrue(BlueprintCost.fits(ok));
		ok.set(16, 0, 0, "stone");
		assertFalse(BlueprintCost.fits(ok));
		assertTrue(BlueprintCost.fits(new Blueprint()));
	}

	@Test
	void betaalbaarEnTekort() {
		Blueprint b = kamer();
		int nodig = BlueprintCost.materials(b).get("stone");
		assertTrue(BlueprintCost.affordable(b, Map.of("stone", nodig)));
		assertEquals(Map.of("stone", 5), BlueprintCost.missing(b, Map.of("stone", nodig - 5)));
		assertEquals(Map.of("stone", nodig), BlueprintCost.missing(b, Map.of("stone", -4)));
	}
}
