package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.Recognition.Kind;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RecognitionTest {
	@Test
	void eisGroeitMetHetNiveau() {
		assertEquals(0, Recognition.required(0));
		assertEquals(1, Recognition.required(1));
		assertEquals(1, Recognition.required(2));
		assertEquals(2, Recognition.required(3));
		assertEquals(2, Recognition.required(4));
		assertEquals(3, Recognition.required(5));
	}

	@Test
	void verwoestRijkErkentAltijd() {
		assertTrue(Recognition.recognised(0, Map.of()));
	}

	@Test
	void zonderBouwwerkenGeenErkenning() {
		assertFalse(Recognition.recognised(2, Map.of()));
		assertEquals(Kind.TROONZAAL, Recognition.missing(2, Map.of()));
	}

	@Test
	void teWeinigBlokkenTelNietMee() {
		assertFalse(Recognition.recognised(2, Map.of(Kind.TROONZAAL, Kind.TROONZAAL.minBlocks() - 1)));
		assertTrue(Recognition.recognised(2, Map.of(Kind.TROONZAAL, Kind.TROONZAAL.minBlocks())));
	}

	@Test
	void groterRijkEistMeerSoorten() {
		var enkel = Map.of(Kind.HAVEN, 150);
		assertTrue(Recognition.recognised(2, enkel));
		assertFalse(Recognition.recognised(3, enkel));
		assertEquals(Kind.TROONZAAL, Recognition.missing(3, enkel));
		var alles = Map.of(Kind.TROONZAAL, 120, Kind.HAVEN, 150, Kind.KAZERNE, 200);
		assertTrue(Recognition.recognised(5, alles));
		assertNull(Recognition.missing(5, alles));
	}
}
