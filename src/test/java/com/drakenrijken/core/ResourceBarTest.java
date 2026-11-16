package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ResourceBarTest {
	@Test
	void uitgevenEnHerstellen() {
		ResourceBar b = new ResourceBar(100);
		assertTrue(b.spend(30));
		assertFalse(b.spend(80));
		assertEquals(70, b.value());
		b.regen(1000);
		assertEquals(100, b.value());
	}

	@Test
	void ongeldigeWaardenWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new ResourceBar(0));
		ResourceBar b = new ResourceBar(10);
		assertThrows(IllegalArgumentException.class, () -> b.spend(-1));
		assertThrows(IllegalArgumentException.class, () -> b.regen(-1));
	}

	@Test
	void magierHeeftManaAndersUithouding() {
		assertEquals(100, ResourceBar.forClass(PlayerClass.MAGIER).max());
		assertEquals(80, ResourceBar.forClass(PlayerClass.RIDDER).max());
	}
}
