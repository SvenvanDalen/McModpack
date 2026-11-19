package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PlayerClassTest {
	@Test
	void parseKentNieuweKlassen() {
		assertEquals(Optional.of(PlayerClass.SAMOERAI), PlayerClass.parse("samoerai"));
		assertEquals(Optional.of(PlayerClass.ONMYOJI), PlayerClass.parse("ONMYOJI"));
	}

	@Test
	void oudeKlassenBlijvenBestaan() {
		assertEquals(PlayerClass.RIDDER, PlayerClass.valueOf("RIDDER"));
		assertEquals(PlayerClass.MAGIER, PlayerClass.valueOf("MAGIER"));
		assertEquals(PlayerClass.SCHURK, PlayerClass.valueOf("SCHURK"));
	}
}
