package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ArmyTest {
	@Test
	void aanwervenStopptBijMaximum() {
		assertEquals(30, Army.recruit(10, 20));
		assertEquals(Army.MAX_SOLDIERS, Army.recruit(190, 50));
	}

	@Test
	void zichtbareSoldatenBlijvenBinnenBudget() {
		assertEquals(5, Army.visible(5));
		assertEquals(EntityBudget.MAX_NPCS, Army.visible(200));
	}

	@Test
	void betereUitrustingMaaktSterker() {
		assertEquals(10, Army.strength(10, Army.Gear.NONE, 10));
		assertEquals(50, Army.strength(10, Army.Gear.STEEL, 10));
	}

	@Test
	void hongerigLegerVechtMinder() {
		assertEquals(10, Army.foodNeeded(10));
		assertEquals(15, Army.strength(10, Army.Gear.IRON, 5));
		assertEquals(0, Army.strength(10, Army.Gear.STEEL, 0));
	}

	@Test
	void negatieveAantallenZijnEenFout() {
		assertThrows(IllegalArgumentException.class, () -> Army.recruit(-1, 1));
		assertThrows(IllegalArgumentException.class, () -> Army.visible(-1));
		assertThrows(IllegalArgumentException.class, () -> Army.strength(1, Army.Gear.NONE, -1));
	}
}
