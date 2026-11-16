package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.Handbook;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class HandbookTest {
	@Test
	void meegeleverdeHandleidingLaadt() throws IOException {
		String tekst = new String(HandbookTest.class.getResourceAsStream("/data/drakenrijken/handbook/handleiding.json").readAllBytes());
		List<String> warns = new ArrayList<>();
		Handbook h = HandbookLoader.load(tekst, warns::add);
		assertTrue(warns.isEmpty());
		assertEquals(5, h.chapters().size());
		assertTrue(h.find("KLASSEN").isPresent());
		assertTrue(h.index().contains("draak"));
		assertTrue(h.describe(h.find("rijk").orElseThrow()).contains("niveau"));
		assertFalse(h.find("bestaatniet").isPresent());
	}

	@Test
	void kapotteTekstGeeftLegeHandleiding() {
		for (String t : List.of("{kapot", "", "null", "[]", "{\"chapters\":[{\"id\":\"\"}]}")) {
			List<String> warns = new ArrayList<>();
			Handbook h = HandbookLoader.load(t, warns::add);
			assertTrue(h.chapters().isEmpty(), t);
			assertFalse(warns.isEmpty(), t);
		}
	}
}
