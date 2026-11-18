package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import com.drakenrijken.core.SiegeWeapon.Design;
import org.junit.jupiter.api.Test;

class SiegeWeaponTest {
	@Test
	void langereArmGeeftMeerBereik() {
		assertTrue(SiegeWeapon.range(new Design(8, 4, 4)) > SiegeWeapon.range(new Design(2, 4, 4)));
		assertEquals(24, SiegeWeapon.range(new Design(4, 4, 4)));
	}

	@Test
	void zwaarderContragewichtGeeftMeerSchade() {
		assertTrue(SiegeWeapon.damage(new Design(4, 8, 4)) > SiegeWeapon.damage(new Design(4, 2, 4)));
		assertEquals(10, SiegeWeapon.damage(new Design(4, 4, 4)));
	}

	@Test
	void gewichtIsSomVanOnderdelen() {
		assertEquals(16, SiegeWeapon.weight(new Design(4, 4, 4)));
	}

	@Test
	void teLichtFrameBreekt() {
		assertFalse(SiegeWeapon.sturdy(new Design(4, 10, 2)));
		assertTrue(SiegeWeapon.sturdy(new Design(4, 10, 5)));
	}

	@Test
	void tezwaarWapenIsNietTeVerplaatsen() {
		var zwaar = new Design(12, 12, 20);
		assertFalse(SiegeWeapon.movable(zwaar));
		assertFalse(SiegeWeapon.usable(zwaar));
		assertTrue(SiegeWeapon.usable(new Design(6, 6, 6)));
	}

	@Test
	void ongeldigOntwerpIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> new Design(0, 4, 4));
		assertThrows(IllegalArgumentException.class, () -> new Design(4, 13, 4));
		assertThrows(IllegalArgumentException.class, () -> new Design(4, 4, 0));
	}
}
