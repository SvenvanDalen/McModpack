package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParticleBudgetTest {
	@Test
	void begrenstPerTickEnHerstelt() {
		ParticleBudget b = new ParticleBudget(true, 10);
		assertEquals(8, b.request(8));
		assertEquals(2, b.request(5));
		assertEquals(0, b.request(1));
		b.nextTick();
		assertEquals(10, b.request(99));
	}

	@Test
	void uitgezetGeeftNul() {
		assertEquals(0, new ParticleBudget(false, 10).request(5));
	}

	@Test
	void ongeldigeInvoer() {
		assertEquals(0, new ParticleBudget(true, 10).request(-3));
		assertThrows(IllegalArgumentException.class, () -> new ParticleBudget(true, -1));
	}
}
