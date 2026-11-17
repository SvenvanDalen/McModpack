package com.drakenrijken.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GenerateFlagTest {
	@Test
	void generatieStaatStandaardUit() {
		assertFalse(ModConfig.defaults().generateStructures());
		assertFalse(ModConfig.parse("{\"particles\": true}").generateStructures());
		assertFalse(ModConfig.parse("{\"generateStructures\": \"ja\"}").generateStructures());
	}

	@Test
	void generatieKanAan() {
		assertTrue(ModConfig.parse("{\"generateStructures\": true}").generateStructures());
	}
}
