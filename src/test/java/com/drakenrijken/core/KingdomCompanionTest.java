package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class KingdomCompanionTest {
	@Test
	void deterministischEnVerdeeld() {
		Set<String> kinds = new java.util.HashSet<>();
		int leeg = 0;
		for (int i = 0; i < 600; i++) {
			String a = KingdomPlacement.companion(42L, i, -i);
			assertEquals(a, KingdomPlacement.companion(42L, i, -i));
			assertTrue(Set.of("", "burcht", "dungeon", "drakengebied").contains(a));
			kinds.add(a);
			if (a.isEmpty()) leeg++;
		}
		assertEquals(4, kinds.size());
		assertTrue(leeg > 200 && leeg < 400);
	}
}
