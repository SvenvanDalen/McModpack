package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Kapotte JSON laat het spel starten en geeft een waarschuwing (DR-902). */
class BrokenDataTest {
	private static final List<String> KAPOT = List.of("{kapot", "", "null", "[]", "42", "{\"id\":");

	private static void controleer(String naam, BiFunction<Stream<String>, Consumer<String>, List<?>> loader) {
		for (String tekst : KAPOT) {
			List<String> warns = new ArrayList<>();
			List<?> result = assertDoesNotThrow(() -> loader.apply(Stream.of(tekst), warns::add), naam + " met '" + tekst + "'");
			assertTrue(result.isEmpty(), naam + " gaf data voor '" + tekst + "'");
			assertFalse(warns.isEmpty(), naam + " waarschuwde niet voor '" + tekst + "'");
		}
	}

	@Test
	void alleLoadersOverleven() {
		controleer("SkillLoader", SkillLoader::load);
		controleer("QuestLoader", QuestLoader::load);
		controleer("DialogueLoader", DialogueLoader::load);
		controleer("MachineRecipeLoader", MachineRecipeLoader::load);
		controleer("StartKitLoader", StartKitLoader::load);
	}

	@Test
	void spawnlijstenOverleven() {
		for (String tekst : KAPOT) {
			List<String> warns = new ArrayList<>();
			var result = assertDoesNotThrow(() -> SpawnTableLoader.loadAll(Stream.of(tekst), warns::add), tekst);
			assertTrue(result.isEmpty(), tekst);
			assertFalse(warns.isEmpty(), tekst);
		}
	}
}
