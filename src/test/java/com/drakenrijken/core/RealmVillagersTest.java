package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RealmVillagersTest {
	@Test
	void groeitMetNiveau() {
		assertEquals(4, RealmVillagers.toSpawn(0, 0));
		assertEquals(6, RealmVillagers.toSpawn(1, 0));
		assertEquals(2, RealmVillagers.toSpawn(1, 4));
	}

	@Test
	void nooitBovenNpcGrens() {
		assertEquals(EntityBudget.MAX_NPCS, RealmVillagers.target(5));
		assertEquals(0, RealmVillagers.toSpawn(5, 16));
	}

	@Test
	void nooitNegatief() {
		assertEquals(0, RealmVillagers.toSpawn(0, 99));
		assertEquals(4, RealmVillagers.toSpawn(0, -3));
	}
}
