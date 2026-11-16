package com.drakenrijken;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RooktestTest {
	@Test
	void modIdIsDrakenrijken() {
		assertEquals("drakenrijken", DrakenRijken.MOD_ID);
	}
}
