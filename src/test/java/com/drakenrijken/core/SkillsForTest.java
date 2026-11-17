package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SkillsForTest {
	@Test
	void geeftAlleenKlasseVaardighedenGesorteerd() {
		var t = new SkillTree(List.of(
			new Skill("b", PlayerClass.MAGIER, List.of()),
			new Skill("a", PlayerClass.MAGIER, List.of("b")),
			new Skill("x", PlayerClass.RIDDER, List.of())));
		assertEquals(List.of("a", "b"), t.skillsFor(PlayerClass.MAGIER).stream().map(Skill::id).toList());
		assertEquals(1, t.skillsFor(PlayerClass.RIDDER).size());
		assertEquals(0, t.skillsFor(PlayerClass.SCHURK).size());
	}
}
