package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShipCrewTest {
	private static final ShipDesign.Design SCHIP = new ShipDesign.Design(20, 6, 40, 10);

	@Test
	void zonderBemanningVaartHetSchipNiet() {
		assertEquals(0, ShipCrew.speed(SCHIP, 1));
		assertTrue(ShipCrew.speed(SCHIP, 2) > 0);
	}

	@Test
	void bemanningEnKanonnenMoetenPassen() {
		assertTrue(ShipCrew.fits(30, 20, 10));
		assertFalse(ShipCrew.fits(30, 20, 11));
	}

	@Test
	void kanonnenHebbenSchuttersNodig() {
		assertEquals(0, ShipCrew.firingCannons(1, 5));
		assertEquals(0, ShipCrew.firingCannons(2, 5));
		assertEquals(2, ShipCrew.firingCannons(6, 5));
		assertEquals(3, ShipCrew.firingCannons(20, 3));
	}

	@Test
	void negatiefIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> ShipCrew.canSail(-1));
		assertThrows(IllegalArgumentException.class, () -> ShipCrew.fits(1, -1, 0));
		assertThrows(IllegalArgumentException.class, () -> ShipCrew.firingCannons(1, -1));
	}
}
