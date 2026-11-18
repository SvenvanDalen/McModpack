package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UnrestTest {
	@Test
	void goedeBehandelingLaatOnrustZakken() {
		assertEquals(40, Unrest.next(50, true, true, false));
		assertEquals(0, Unrest.next(5, true, true, false));
	}

	@Test
	void slechteBehandelingLaatOnrustOplopen() {
		assertEquals(60, Unrest.next(50, false, true, false));
		assertEquals(75, Unrest.next(50, false, false, true));
	}

	@Test
	void onrustBlijftBinnenGrenzen() {
		assertEquals(Unrest.MAX, Unrest.next(95, false, false, true));
	}

	@Test
	void opstandBreektUitVanafDrempel() {
		assertFalse(Unrest.revolts(79));
		assertTrue(Unrest.revolts(80));
	}

	@Test
	void opstandKostBewakers() {
		assertEquals(5, Unrest.guardsLost(10, 20));
		assertEquals(2, Unrest.guardsLost(2, 100));
	}

	@Test
	void ongeldigeInvoerIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> Unrest.next(-1, true, true, true));
		assertThrows(IllegalArgumentException.class, () -> Unrest.next(101, true, true, true));
		assertThrows(IllegalArgumentException.class, () -> Unrest.guardsLost(-1, 0));
	}
}
