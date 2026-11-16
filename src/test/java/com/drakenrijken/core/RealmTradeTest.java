package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RealmTradeTest {
	@Test
	void groeitPerNiveau() {
		assertEquals(0, RealmTrade.goodwill(0));
		assertEquals(4, RealmTrade.goodwill(1));
		assertEquals(20, RealmTrade.goodwill(5));
	}

	@Test
	void ongeldigNiveau() {
		assertThrows(IllegalArgumentException.class, () -> RealmTrade.goodwill(-1));
		assertThrows(IllegalArgumentException.class, () -> RealmTrade.goodwill(6));
	}
}
