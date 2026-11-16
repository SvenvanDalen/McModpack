package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EraGateTest {
	private final EraGate gate = new EraGate("stoom", "rune");

	@Test
	void tijdperkVolgtVolbrachteQuests() {
		QuestProgress q = new QuestProgress();
		assertEquals(1, gate.currentEra(q));
		q.completed().add("stoom");
		assertEquals(2, gate.currentEra(q));
		q.completed().add("rune");
		assertEquals(3, gate.currentEra(q));
	}

	@Test
	void tijdperk3ZonderTijdperk2BlijftDicht() {
		QuestProgress q = new QuestProgress();
		q.completed().add("rune");
		assertEquals(1, gate.currentEra(q));
		assertFalse(gate.isUnlocked(q, 2));
		assertTrue(gate.isUnlocked(q, 1));
	}

	@Test
	void ongeldigeInvoerWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> gate.isUnlocked(new QuestProgress(), 4));
		assertThrows(IllegalArgumentException.class, () -> new EraGate("", "x"));
	}
}
