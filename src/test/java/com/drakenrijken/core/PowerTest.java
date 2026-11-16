package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class PowerTest {
	@Test
	void waterradLevertKrachtPerZijdeMetWater() {
		assertEquals(0, WaterWheel.power(0));
		assertEquals(2, WaterWheel.power(1));
		assertEquals(8, WaterWheel.power(4));
		assertThrows(IllegalArgumentException.class, () -> WaterWheel.power(5));
		assertThrows(IllegalArgumentException.class, () -> WaterWheel.power(-1));
	}

	@Test
	void netwerkTeltBronnenOpEnTrektWrijvingAf() {
		assertEquals(6, new PowerNetwork(List.of(2, 4), 0).available());
		assertEquals(4, new PowerNetwork(List.of(2, 4), 8).available());
		assertEquals(0, new PowerNetwork(List.of(1), 32).available());
		assertEquals(0, new PowerNetwork(List.of(), 0).available());
	}

	@Test
	void netwerkKanVraagLeverenOfNiet() {
		PowerNetwork n = new PowerNetwork(List.of(4), 4);
		assertTrue(n.canSupply(3));
		assertFalse(n.canSupply(4));
	}

	@Test
	void ongeldigNetwerkWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new PowerNetwork(List.of(1), 33));
		assertThrows(IllegalArgumentException.class, () -> new PowerNetwork(List.of(-1), 0));
		assertThrows(IllegalArgumentException.class, () -> new PowerNetwork(List.of(1), 0).canSupply(-1));
	}

	@Test
	void enormeBronnenLopenNietOver() {
		assertEquals(Integer.MAX_VALUE, new PowerNetwork(List.of(Integer.MAX_VALUE, Integer.MAX_VALUE), 0).available());
	}
}
