package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class JapaneseFoodTest {
	@Test
	void viergerechtenBestaan() {
		Set<String> ids = JapaneseFood.all().stream().map(JapaneseFood.Stats::id).collect(Collectors.toSet());
		assertEquals(Set.of("rijst", "onigiri", "sushi", "thee"), ids);
		assertEquals(4, JapaneseFood.all().size());
	}

	@Test
	void voedingswaardenZijnPositief() {
		for (JapaneseFood.Stats s : JapaneseFood.all()) {
			assertTrue(s.hunger() > 0, s.id());
			assertTrue(s.saturation() > 0, s.id());
			assertFalse(s.bonus().isBlank(), s.id());
		}
	}

	@Test
	void theeGeeftManaHerstel() {
		assertEquals("mana_herstel", JapaneseFood.get("thee").bonus());
		assertEquals("uithouding_herstel", JapaneseFood.get("onigiri").bonus());
		assertEquals("snelheid", JapaneseFood.get("sushi").bonus());
	}

	@Test
	void receptenBestaanVoorElkGerecht() throws IOException, URISyntaxException {
		Path dir = Paths.get(JapaneseFoodTest.class.getResource("/data/drakenrijken/recipe").toURI());
		for (JapaneseFood.Stats s : JapaneseFood.all()) {
			Path p = dir.resolve(s.id() + ".json");
			assertTrue(Files.exists(p), "recept ontbreekt: " + s.id());
			assertTrue(Files.readString(p).contains("drakenrijken:" + s.id()), s.id());
		}
	}
}
