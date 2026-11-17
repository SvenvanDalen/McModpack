package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuestTrackerTest {
	@Test
	void toontEersteActieveQuestEnLeegAlsErGeenIs() {
		var a = new QuestDef("een", List.of(), "kill", "zombie", 3);
		var b = new QuestDef("twee", List.of("een"), "kill", "spin", 2);
		var p = new QuestProgress();
		assertEquals("een: 0/3", QuestTracker.line(List.of(a, b), p));
		p.counts().put("een", 1);
		assertEquals("een: 1/3", QuestTracker.line(List.of(a, b), p));
		p.completed().add("een");
		assertEquals("twee: 0/2", QuestTracker.line(List.of(a, b), p));
		p.completed().add("twee");
		assertEquals("", QuestTracker.line(List.of(a, b), p));
		assertEquals("", QuestTracker.line(List.of(), p));
	}
}
