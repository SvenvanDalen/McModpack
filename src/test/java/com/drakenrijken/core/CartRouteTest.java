package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class CartRouteTest {
	@Test
	void opbrengstPerMinuutEnVolledigeRondes() {
		var r = new CartRoute(16, 30);
		assertEquals(32, r.perMinute());
		assertEquals(16, r.deliveredIn(59));
		assertEquals(32, r.deliveredIn(60));
		assertEquals(0, r.deliveredIn(-10));
	}

	@Test
	void ongeldigeWaardenWordenGeweigerd() {
		assertThrows(IllegalArgumentException.class, () -> new CartRoute(0, 10));
		assertThrows(IllegalArgumentException.class, () -> new CartRoute(10, 0));
	}

	@Test
	void hoogstensVierKarrenTellen() {
		var r = new CartRoute(10, 60);
		assertEquals(40, CartRoute.totalPerMinute(List.of(r, r, r, r, r, r)));
	}
}
