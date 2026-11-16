package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class QuestLogTest {
	private final List<QuestDef> quests = List.of(
		new QuestDef("hout", List.of(), "break_block", "minecraft:oak_log", 5),
		new QuestDef("smederij", List.of("hout"), "craft", "forge", 1));

	@Test
	void actieveQuestToontVoortgangEnVervolgIsVerborgen() {
		QuestProgress p = new QuestProgress();
		p.counts().put("hout", 2);
		assertEquals(List.of("[bezig] hout: break_block minecraft:oak_log (2/5)"), QuestLog.lines(quests, p));
	}

	@Test
	void voltooidStaatNaActieveEnOntsluitVervolg() {
		QuestProgress p = new QuestProgress();
		p.completed().add("hout");
		List<String> l = QuestLog.lines(quests, p);
		assertEquals(2, l.size());
		assertTrue(l.get(0).startsWith("[bezig] smederij"));
		assertEquals("[klaar] hout", l.get(1));
	}

	@Test
	void klasseParseIsHoofdletterOnafhankelijk() {
		assertEquals(Optional.of(PlayerClass.MAGIER), PlayerClass.parse(" Magier "));
		assertEquals(Optional.empty(), PlayerClass.parse("draak"));
		assertEquals(Optional.empty(), PlayerClass.parse(null));
	}
}
