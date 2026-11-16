package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RealmEffectsTest {
	@Test
	void prijsDaaltMetHetNiveau() {
		assertEquals(100, RealmEffects.price(100, 0));
		assertEquals(75, RealmEffects.price(100, 5));
		assertEquals(90, RealmEffects.price(100, 2));
	}

	@Test
	void prijsIsNooitNulBehalveGratis() {
		assertEquals(1, RealmEffects.price(1, 5));
		assertEquals(0, RealmEffects.price(0, 5));
	}

	@Test
	void ongeldigeInvoerWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> RealmEffects.price(10, 6));
		assertThrows(IllegalArgumentException.class, () -> RealmEffects.price(10, -1));
		assertThrows(IllegalArgumentException.class, () -> RealmEffects.price(-1, 1));
	}
}
