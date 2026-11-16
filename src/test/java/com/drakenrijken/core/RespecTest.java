package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class RespecTest {
	private final SkillTree tree = new SkillTree(List.of(
		new Skill("a", PlayerClass.RIDDER, List.of()),
		new Skill("b", PlayerClass.RIDDER, List.of("a"))));

	private PlayerProgress withTwoSkills() {
		PlayerProgress p = new PlayerProgress(5, 0);
		assertEquals(SkillTree.Result.OK, tree.unlock(p, PlayerClass.RIDDER, "a"));
		assertEquals(SkillTree.Result.OK, tree.unlock(p, PlayerClass.RIDDER, "b"));
		return p;
	}

	@Test
	void prijsGroeitMetAantalVaardigheden() {
		assertEquals(0, SkillTree.respecCost(new PlayerProgress()));
		assertEquals(20, SkillTree.respecCost(withTwoSkills()));
	}

	@Test
	void teWeinigMuntenVerandertNiets() {
		PlayerProgress p = withTwoSkills();
		assertEquals(-1, SkillTree.respec(p, 19));
		assertEquals(2, p.unlocked().size());
	}

	@Test
	void betalenGeeftAllePuntenTerug() {
		PlayerProgress p = withTwoSkills();
		assertEquals(2, 4 - tree.pointsLeft(p));
		assertEquals(20, SkillTree.respec(p, 20));
		assertTrue(p.unlocked().isEmpty());
		assertEquals(4, tree.pointsLeft(p));
		assertEquals(SkillTree.Result.OK, tree.unlock(p, PlayerClass.RIDDER, "a"));
	}

	@Test
	void gratisAlsErNietsOntgrendeldIs() {
		assertEquals(0, SkillTree.respec(new PlayerProgress(), 0));
	}
}
