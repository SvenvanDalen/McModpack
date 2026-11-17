package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PipeTest {
	@Test
	void lengteGrenzen() {
		assertFalse(Pipe.validLength(0));
		assertTrue(Pipe.validLength(1));
		assertTrue(Pipe.validLength(Pipe.MAX_LENGTH));
		assertFalse(Pipe.validLength(Pipe.MAX_LENGTH + 1));
	}

	@Test
	void doorvoerIsHetMinimum() {
		assertEquals(3, Pipe.delivered(5, 3, 8));
		assertEquals(4, Pipe.delivered(5, 8, 4));
		assertEquals(Pipe.THROUGHPUT_PER_TICK, Pipe.delivered(5, 100, 100));
	}

	@Test
	void ongeldigOfNegatiefGeeftNul() {
		assertEquals(0, Pipe.delivered(Pipe.MAX_LENGTH + 1, 5, 5));
		assertEquals(0, Pipe.delivered(3, -2, 5));
	}
}
