package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MachineRecipeBookTest {
	private static MachineRecipe ijzer() {
		return new MachineRecipe("ijzer", "smederij", Map.of("erts", 1, "kool", 1), "staaf", 1, 200, 2);
	}

	@Test
	void vindtReceptAlsAllesAanwezigIs() {
		MachineRecipeBook b = new MachineRecipeBook(List.of(ijzer()));
		assertEquals("ijzer", b.find("smederij", Map.of("erts", 3, "kool", 1)).orElseThrow().id());
	}

	@Test
	void vindtNietBijTekortOfAndereMachine() {
		MachineRecipeBook b = new MachineRecipeBook(List.of(ijzer()));
		assertTrue(b.find("smederij", Map.of("erts", 1)).isEmpty());
		assertTrue(b.find("pers", Map.of("erts", 1, "kool", 1)).isEmpty());
		assertEquals(1, b.forMachine("smederij").size());
	}

	@Test
	void ongeldigeReceptenWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new MachineRecipe("x", "m", Map.of(), "o", 1, 1, 0));
		assertThrows(IllegalArgumentException.class, () -> new MachineRecipe("x", "m", Map.of("a", 0), "o", 1, 1, 0));
		assertThrows(IllegalArgumentException.class, () -> new MachineRecipe("x", "m", Map.of("a", 1), "o", 1, 0, 0));
		assertThrows(IllegalArgumentException.class, () -> new MachineRecipeBook(List.of(ijzer(), ijzer())));
	}
}
