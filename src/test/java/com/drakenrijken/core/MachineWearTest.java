package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MachineWearTest {
	@Test
	void geenStoringVoorDeDrempel() {
		MachineWear w = new MachineWear(true);
		for (int i = 0; i < MachineWear.THRESHOLD - 1; i++) assertFalse(w.onProduct(0.0));
		assertEquals(0, w.chancePercent() - 0);
		assertFalse(w.broken());
	}

	@Test
	void naDeDrempelKanHijStukgaan() {
		MachineWear w = new MachineWear(true);
		for (int i = 0; i < MachineWear.THRESHOLD - 1; i++) w.onProduct(0.99);
		assertTrue(w.onProduct(0.0));
		assertTrue(w.broken());
		assertEquals("Storing", w.describe());
	}

	@Test
	void hoogeWorpBlijftHeel() {
		MachineWear w = new MachineWear(true);
		for (int i = 0; i < 500; i++) assertFalse(w.onProduct(0.999));
		assertEquals(MachineWear.MAX_CHANCE_PERCENT, w.chancePercent());
	}

	@Test
	void kapotMaaktNietsEnHerstelZetTerug() {
		MachineWear w = new MachineWear(true);
		for (int i = 0; i < MachineWear.THRESHOLD - 1; i++) w.onProduct(0.99);
		w.onProduct(0.0);
		int slijtage = w.wear();
		assertFalse(w.onProduct(0.0));
		assertEquals(slijtage, w.wear());
		assertTrue(w.repair());
		assertFalse(w.broken());
		assertEquals(0, w.wear());
		assertFalse(w.repair());
	}

	@Test
	void uitgezetGebeurtNiets() {
		MachineWear w = new MachineWear(false);
		for (int i = 0; i < 500; i++) assertFalse(w.onProduct(0.0));
		assertEquals(0, w.wear());
	}
}
