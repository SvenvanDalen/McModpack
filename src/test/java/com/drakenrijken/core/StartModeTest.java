package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import com.drakenrijken.config.ModConfig;
import org.junit.jupiter.api.Test;

class StartModeTest {
	@Test
	void legeKavelGenereertGeenStructuren() {
		assertFalse(StartMode.EMPTY_PLOT.generatesStructures());
		assertTrue(StartMode.NORMAL.generatesStructures());
	}

	@Test
	void onbekendeWaardeIsNormaleStart() {
		assertEquals(StartMode.EMPTY_PLOT, StartMode.parse("lege-kavel"));
		assertEquals(StartMode.NORMAL, StartMode.parse("onzin"));
		assertEquals(StartMode.NORMAL, StartMode.parse(null));
	}

	@Test
	void configLeestStartmodus() {
		assertEquals(StartMode.NORMAL, ModConfig.parse("{}").startMode());
		assertEquals(StartMode.EMPTY_PLOT, ModConfig.parse("{\"startMode\": \"lege-kavel\"}").startMode());
	}

	@Test
	void structurenAlleenBijAanEnNormaleStart() {
		assertTrue(ModConfig.parse("{\"generateStructures\": true}").buildsStructures());
		assertFalse(ModConfig.parse("{\"generateStructures\": true, \"startMode\": \"lege-kavel\"}").buildsStructures());
		assertFalse(ModConfig.parse("{\"startMode\": \"normaal\"}").buildsStructures());
	}
}
