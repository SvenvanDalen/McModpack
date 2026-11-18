package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import com.drakenrijken.core.ShipDesign.Design;
import org.junit.jupiter.api.Test;

class ShipDesignTest {
	@Test
	void groterKielGeeftMeerDrijfkracht() {
		assertTrue(ShipDesign.buoyancy(new Design(20, 6, 40, 10)) > ShipDesign.buoyancy(new Design(10, 6, 40, 10)));
	}

	@Test
	void teZwaarSchipZinktEnVaartNiet() {
		var zwaar = new Design(5, 2, 100, 10);
		assertFalse(ShipDesign.floats(zwaar));
		assertEquals(0, ShipDesign.speed(zwaar));
	}

	@Test
	void meerZeilGeeftMeerSnelheid() {
		assertTrue(ShipDesign.speed(new Design(20, 6, 60, 20)) > ShipDesign.speed(new Design(20, 6, 60, 5)));
	}

	@Test
	void kleinSchipDraaitBeterDanGroot() {
		assertTrue(ShipDesign.maneuverability(new Design(8, 3, 20, 4)) > ShipDesign.maneuverability(new Design(30, 10, 20, 4)));
	}

	@Test
	void groterSchipHeeftMeerPlekken() {
		assertEquals(30, ShipDesign.slots(new Design(20, 6, 40, 10)));
		assertTrue(ShipDesign.slots(new Design(30, 8, 40, 10)) > ShipDesign.slots(new Design(10, 4, 40, 10)));
	}

	@Test
	void ongeldigOntwerpIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> new Design(2, 1, 0, 0));
		assertThrows(IllegalArgumentException.class, () -> new Design(10, 11, 0, 0));
		assertThrows(IllegalArgumentException.class, () -> new Design(10, 4, -1, 0));
	}
}
