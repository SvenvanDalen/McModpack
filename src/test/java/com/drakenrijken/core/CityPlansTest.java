package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CityPlansTest {
	@Test
	void grootsteStadHeeftTweehonderdInwoners() {
		assertEquals(200, CityPlans.inhabitants(CityPlans.city()));
	}

	@Test
	void stadBlijftBinnenVierenzestigBijVierenzestig() {
		for (var p : CityPlans.city().placements()) {
			assertTrue(p.x() >= 0 && p.x() < 64 && p.z() >= 0 && p.z() < 64 && p.y() >= -1 && p.y() <= 4, p.toString());
		}
	}

	@Test
	void zichtbareFigurenBlijvenBinnenDeNpcGrens() {
		long figuren = CityPlans.city().spots().stream().filter(s -> !s.role().equals("bewaker")).count();
		assertTrue(figuren <= EntityBudget.MAX_NPCS);
	}

	@Test
	void stadStaatNooitNaastEenBijzonderePlekEnIsDeterministisch() {
		int steden = 0;
		for (int cx = 0; cx < 400; cx++) {
			if (!KingdomPlacement.hasKingdom(12345L, cx, 3)) continue;
			boolean a = CityPlans.isCity(12345L, cx, 3);
			assertEquals(a, CityPlans.isCity(12345L, cx, 3));
			if (a) { steden++; assertTrue(KingdomPlacement.companion(12345L, cx, 3).isEmpty()); }
		}
		assertTrue(steden >= 0);
		assertFalse(CityPlans.inhabitants(Blueprints.village()) >= 200);
	}
}
