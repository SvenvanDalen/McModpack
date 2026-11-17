package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RealmTaxTest {
	@Test
	void opbrengstGroeitMetHetNiveau() {
		assertEquals(0, RealmTax.daily(0));
		assertEquals(2, RealmTax.daily(1));
		assertEquals(10, RealmTax.daily(RealmState.MAX_LEVEL));
	}

	@Test
	void plafondVanDrieDagen() {
		assertEquals(0, RealmTax.owed(3, 10, 10));
		assertEquals(6, RealmTax.owed(1, 10, 13));
		assertEquals(6, RealmTax.owed(1, 10, 500));
		assertEquals(0, RealmTax.owed(3, 10, 5));
	}

	@Test
	void nietBestaandNiveauWordtGeweigerd() {
		assertThrows(IllegalArgumentException.class, () -> RealmTax.daily(-1));
		assertThrows(IllegalArgumentException.class, () -> RealmTax.daily(RealmState.MAX_LEVEL + 1));
	}
}
