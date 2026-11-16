package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MachineBudgetTest {
	@Test
	void de65eActivatieMislukt() {
		MachineBudget b = new MachineBudget();
		for (int i = 0; i < 64; i++) {
			assertTrue(b.tryActivate());
		}
		assertFalse(b.tryActivate());
		assertEquals(64, b.active());
	}

	@Test
	void naReleaseLuktEenNieuwe() {
		MachineBudget b = new MachineBudget();
		for (int i = 0; i < 64; i++) {
			b.tryActivate();
		}
		b.release();
		assertTrue(b.tryActivate());
		assertFalse(b.tryActivate());
	}

	@Test
	void releaseZonderActieveMachinesGaatNietOnderNul() {
		MachineBudget b = new MachineBudget();
		b.release();
		assertEquals(0, b.active());
	}
}
