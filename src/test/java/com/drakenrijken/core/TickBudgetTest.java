package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TickBudgetTest {
	@Test
	void stoptAlsDeTijdOpIsEnHerstelBijBegin() {
		long[] now = {0};
		TickBudget b = new TickBudget(100, () -> now[0]);
		assertTrue(b.hasTime());
		now[0] = 99;
		assertTrue(b.hasTime());
		now[0] = 100;
		assertFalse(b.hasTime());
		b.begin();
		assertTrue(b.hasTime());
	}

	@Test
	void ongeldigeGrensEnStandaardBudgetten() {
		assertThrows(IllegalArgumentException.class, () -> new TickBudget(0, () -> 0));
		assertTrue(TickBudget.machines().hasTime());
		assertTrue(TickBudget.realm().hasTime());
	}
}
