package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EntityBudgetTest {
	@Test
	void grensPerSoortEnOnafhankelijk() {
		EntityBudget b = new EntityBudget();
		for (int i = 0; i < EntityBudget.MAX_NPCS; i++) assertTrue(b.trySpawn(EntityBudget.Kind.NPC));
		assertFalse(b.trySpawn(EntityBudget.Kind.NPC));
		assertTrue(b.trySpawn(EntityBudget.Kind.ENEMY));
	}

	@Test
	void vrijgevenMaaktRuimteEnZaktNietOnderNul() {
		EntityBudget b = new EntityBudget();
		b.despawn(EntityBudget.Kind.ENEMY);
		assertEquals(0, b.count(EntityBudget.Kind.ENEMY));
		for (int i = 0; i < EntityBudget.MAX_ENEMIES; i++) b.trySpawn(EntityBudget.Kind.ENEMY);
		assertFalse(b.trySpawn(EntityBudget.Kind.ENEMY));
		b.despawn(EntityBudget.Kind.ENEMY);
		assertTrue(b.trySpawn(EntityBudget.Kind.ENEMY));
	}
}
