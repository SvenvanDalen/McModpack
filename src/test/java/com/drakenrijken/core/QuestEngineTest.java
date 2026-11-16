package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuestEngineTest {
	private static final QuestEvent HOUT = new QuestEvent("break_block", "minecraft:oak_log");
	private final QuestEngine engine = new QuestEngine(List.of(
		new QuestDef("hout", List.of(), "break_block", "minecraft:oak_log", 5),
		new QuestDef("smederij", List.of("hout"), "craft", "drakenrijken:forge", 1),
		new QuestDef("wolf", List.of(), "kill", "*", 2)));

	@Test
	void questWordtVoltooidNaVijfEvents() {
		PlayerData d = PlayerData.empty();
		for (int i = 0; i < 4; i++) assertTrue(engine.onEvent(HOUT, d).isEmpty());
		assertEquals(4, d.quests().progress("hout"));
		assertEquals(List.of("hout"), engine.onEvent(HOUT, d));
		assertTrue(d.quests().isCompleted("hout"));
	}

	@Test
	void extraEventsNaVoltooienDoenNiets() {
		PlayerData d = PlayerData.empty();
		for (int i = 0; i < 5; i++) engine.onEvent(HOUT, d);
		assertTrue(engine.onEvent(HOUT, d).isEmpty());
		assertEquals(5, d.quests().progress("hout"));
	}

	@Test
	void ketenWordtInVolgordeAfgedwongen() {
		PlayerData d = PlayerData.empty();
		QuestEvent craft = new QuestEvent("craft", "drakenrijken:forge");
		assertTrue(engine.onEvent(craft, d).isEmpty());
		assertEquals(0, d.quests().progress("smederij"));
		for (int i = 0; i < 5; i++) engine.onEvent(HOUT, d);
		assertEquals(List.of("smederij"), engine.onEvent(craft, d));
	}

	@Test
	void sterDoelTeltAlleSoorten() {
		PlayerData d = PlayerData.empty();
		engine.onEvent(new QuestEvent("kill", "minecraft:zombie"), d);
		assertEquals(List.of("wolf"), engine.onEvent(new QuestEvent("kill", "minecraft:wolf"), d));
	}

	@Test
	void verkeerdDoelTeltNiet() {
		PlayerData d = PlayerData.empty();
		engine.onEvent(new QuestEvent("break_block", "minecraft:stone"), d);
		assertFalse(d.quests().isCompleted("hout"));
		assertEquals(0, d.quests().progress("hout"));
	}

	@Test
	void ongeldigeDefinitiesWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new QuestDef("x", List.of(), "kill", "*", 0));
		assertThrows(IllegalArgumentException.class, () -> new QuestEngine(List.of(
			new QuestDef("a", List.of(), "kill", "*", 1), new QuestDef("a", List.of(), "kill", "*", 1))));
		assertThrows(IllegalArgumentException.class, () -> new QuestEngine(List.of(
			new QuestDef("a", List.of("bestaat-niet"), "kill", "*", 1))));
	}

	@Test
	void beloningGeeftXpAlleenBijVoltooien() {
		QuestEngine e = new QuestEngine(List.of(new QuestDef("a", List.of(), "kill", "*", 2, new QuestReward(50, List.of("minecraft:bread")))));
		PlayerData d = PlayerData.empty();
		e.onEvent(new QuestEvent("kill", "x"), d);
		assertEquals(0, d.progress().xp());
		e.onEvent(new QuestEvent("kill", "x"), d);
		assertEquals(50, d.progress().xp());
		e.onEvent(new QuestEvent("kill", "x"), d);
		assertEquals(50, d.progress().xp());
	}

	@Test
	void negatieveBeloningWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new QuestReward(-1, List.of()));
	}

	@Test
	void beloningGeeftVaardigheidspuntenAlleenBijVoltooien() {
		QuestEngine e = new QuestEngine(List.of(new QuestDef("a", List.of(), "kill", "*", 2, new QuestReward(0, 2, List.of()))));
		PlayerData d = PlayerData.empty();
		e.onEvent(new QuestEvent("kill", "x"), d);
		assertEquals(0, d.progress().bonusPoints());
		e.onEvent(new QuestEvent("kill", "x"), d);
		e.onEvent(new QuestEvent("kill", "x"), d);
		assertEquals(2, d.progress().bonusPoints());
		assertEquals(2, SkillTree.pointsEarned(d.progress()));
	}

	@Test
	void negatievePuntenWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new QuestReward(0, -1, List.of()));
		assertThrows(IllegalArgumentException.class, () -> new PlayerProgress().addBonusPoints(-1));
	}
}
