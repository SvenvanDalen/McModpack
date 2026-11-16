package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class RealmStateTest {
	@Test
	void machtNulGeeftNiveauNul() {
		assertEquals(0, RealmState.levelFor(0));
	}

	@Test
	void niveauIsMonotoonEnBegrensdOp5() {
		int previous = 0;
		for (int p = 0; p <= 5000; p += 7) {
			int l = RealmState.levelFor(p);
			assertTrue(l >= previous && l <= 5);
			previous = l;
		}
		assertEquals(5, RealmState.levelFor(Integer.MAX_VALUE));
	}

	@Test
	void drempelsKloppen() {
		assertEquals(0, RealmState.levelFor(9));
		assertEquals(1, RealmState.levelFor(10));
		assertEquals(5, RealmState.levelFor(1000));
		assertEquals(10, RealmState.nextThreshold(0));
		assertEquals(-1, RealmState.nextThreshold(5));
	}

	@Test
	void negatieveMachtWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> RealmState.levelFor(-1));
	}

	@Test
	void totaleMachtTeltBinnenDeMachinegrens() {
		assertEquals(0, RealmPower.total(List.of()));
		assertEquals(6, RealmPower.total(List.of(1, 2, 3)));
		List<Integer> veel = java.util.Collections.nCopies(100, 2);
		assertEquals(MachineBudget.MAX_ACTIVE * 2, RealmPower.total(veel));
		assertThrows(IllegalArgumentException.class, () -> RealmPower.total(List.of(-1)));
	}
}
