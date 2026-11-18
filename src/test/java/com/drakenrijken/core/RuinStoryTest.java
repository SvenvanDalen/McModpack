package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class RuinStoryTest {
	@Test
	void aanwijzingenInVolgordeDanPasDeVondst() {
		RuinStory s = new RuinStory(List.of("eerste", "tweede"), "ruine_schat");
		assertNull(s.loot());
		assertEquals("eerste", s.nextClue());
		assertNull(s.loot());
		assertEquals("tweede", s.nextClue());
		assertTrue(s.complete());
		assertEquals("ruine_schat", s.loot());
		assertNull(s.nextClue());
	}

	@Test
	void zonderAanwijzingenMagNiet() {
		assertThrows(IllegalArgumentException.class, () -> new RuinStory(List.of(), "x"));
	}

	@Test
	void maximaal12Bij12() {
		Blueprint ok = new Blueprint();
		ok.fill(0, 0, 0, 11, 2, 11, "stone");
		assertTrue(RuinStory.fits(ok));
		Blueprint groot = new Blueprint();
		groot.fill(0, 0, 0, 12, 0, 0, "stone");
		assertFalse(RuinStory.fits(groot));
		assertTrue(RuinStory.fits(new Blueprint()));
	}
}
