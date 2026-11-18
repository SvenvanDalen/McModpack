package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LightningRodTest {
	@Test
	void ladingStijgtTotDeGrens() {
		LightningRod r = new LightningRod();
		for (int i = 0; i < 5; i++) assertEquals(100, r.strike());
		assertEquals(0, r.strike());
		assertEquals(LightningRod.CAPACITY, r.charge());
	}

	@Test
	void opnemenIsAllesOfNiets() {
		LightningRod r = new LightningRod();
		r.strike();
		assertFalse(r.draw(101));
		assertEquals(100, r.charge());
		assertTrue(r.draw(60));
		assertEquals(40, r.charge());
		assertFalse(r.draw(-1));
	}
}
