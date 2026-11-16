package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConveyorTest {
	@Test
	void itemReistEenVakjePerTikEnWordtAfgeleverd() {
		Conveyor c = new Conveyor(3);
		List<String> ontvangen = new ArrayList<>();
		assertTrue(c.insert("ijzer"));
		for (int tik = 0; tik < 2; tik++) c.tick(i -> ontvangen.add(i));
		assertTrue(ontvangen.isEmpty());
		assertEquals("ijzer", c.at(2).orElseThrow());
		c.tick(i -> ontvangen.add(i));
		assertEquals(List.of("ijzer"), ontvangen);
		assertEquals(0, c.count());
	}

	@Test
	void volleBeginvakjeWeigert() {
		Conveyor c = new Conveyor(2);
		assertTrue(c.insert("a"));
		assertFalse(c.insert("b"));
	}

	@Test
	void weigerendeOntvangerLaatDeBandVolLopenZonderVerlies() {
		Conveyor c = new Conveyor(3);
		int gezet = 0;
		for (int tik = 0; tik < 20; tik++) {
			if (c.insert("x" + gezet)) gezet++;
			c.tick(i -> false);
		}
		assertEquals(3, c.count());
		assertEquals(3, gezet);
		List<String> ontvangen = new ArrayList<>();
		for (int tik = 0; tik < 10; tik++) c.tick(i -> ontvangen.add(i));
		assertEquals(List.of("x0", "x1", "x2"), ontvangen);
	}

	@Test
	void volgordeBlijftBehouden() {
		Conveyor c = new Conveyor(4);
		List<String> ontvangen = new ArrayList<>();
		String[] items = {"a", "b", "c", "d", "e"};
		int volgende = 0;
		for (int tik = 0; tik < 30; tik++) {
			if (volgende < items.length && c.insert(items[volgende])) volgende++;
			c.tick(i -> ontvangen.add(i));
		}
		assertEquals(List.of(items), ontvangen);
	}

	@Test
	void ongeldigeInvoerWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new Conveyor(0));
		assertThrows(IllegalArgumentException.class, () -> new Conveyor(Conveyor.MAX_LENGTH + 1));
		assertThrows(IllegalArgumentException.class, () -> new Conveyor(2).insert(" "));
	}
}
