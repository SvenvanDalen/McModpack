package com.drakenrijken.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ModConfigTest {
	@Test
	void standaardwaarden() {
		ModConfig c = ModConfig.defaults();
		assertTrue(c.particles());
		assertEquals(64, c.maxActiveMachines());
	}

	@Test
	void leestSchakelaars() {
		ModConfig c = ModConfig.parse("{\"particles\": false, \"maxActiveMachines\": 20}");
		assertFalse(c.particles());
		assertEquals(20, c.maxActiveMachines());
	}

	@Test
	void ontbrekendeVeldenKrijgenStandaard() {
		ModConfig c = ModConfig.parse("{\"particles\": false}");
		assertEquals(64, c.maxActiveMachines());
	}

	@Test
	void kapotOfOnzinnigGeeftStandaardInsteadOfCrash() {
		assertEquals(ModConfig.defaults(), ModConfig.parse("{kapot"));
		assertEquals(ModConfig.defaults(), ModConfig.parse(""));
		assertEquals(64, ModConfig.parse("{\"maxActiveMachines\": -5}").maxActiveMachines());
		assertEquals(64, ModConfig.parse("{\"maxActiveMachines\": \"veel\"}").maxActiveMachines());
	}
}
