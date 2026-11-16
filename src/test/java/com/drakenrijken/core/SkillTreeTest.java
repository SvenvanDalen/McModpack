package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class SkillTreeTest {
	private final SkillTree tree = new SkillTree(List.of(
		new Skill("taaiheid", PlayerClass.RIDDER, List.of()),
		new Skill("schildstoot", PlayerClass.RIDDER, List.of("taaiheid")),
		new Skill("vuurbal", PlayerClass.MAGIER, List.of())));

	@Test
	void eenPuntPerLevelBoven1() {
		assertEquals(0, SkillTree.pointsEarned(new PlayerProgress()));
		assertEquals(4, SkillTree.pointsEarned(new PlayerProgress(5, 0)));
		assertEquals(29, SkillTree.pointsEarned(new PlayerProgress(30, 0)));
	}

	@Test
	void zonderPuntenGeeftNoPoints() {
		PlayerProgress p = new PlayerProgress();
		assertEquals(SkillTree.Result.NO_POINTS, tree.unlock(p, PlayerClass.RIDDER, "taaiheid"));
		assertEquals(0, p.unlocked().size());
	}

	@Test
	void ontbrekendeVoorwaardeGeeftLocked() {
		PlayerProgress p = new PlayerProgress(5, 0);
		assertEquals(SkillTree.Result.LOCKED, tree.unlock(p, PlayerClass.RIDDER, "schildstoot"));
		assertEquals(0, p.unlocked().size());
	}

	@Test
	void ontgrendelenKostEenPuntEnOntsluitVervolg() {
		PlayerProgress p = new PlayerProgress(3, 0);
		assertEquals(SkillTree.Result.OK, tree.unlock(p, PlayerClass.RIDDER, "taaiheid"));
		assertEquals(1, tree.pointsLeft(p));
		assertEquals(SkillTree.Result.OK, tree.unlock(p, PlayerClass.RIDDER, "schildstoot"));
		assertEquals(0, tree.pointsLeft(p));
		assertTrue(p.unlocked().contains("schildstoot"));
	}

	@Test
	void dubbelOntgrendelenEnVreemdeIdsWordenAfgewezen() {
		PlayerProgress p = new PlayerProgress(5, 0);
		tree.unlock(p, PlayerClass.RIDDER, "taaiheid");
		assertEquals(SkillTree.Result.ALREADY_UNLOCKED, tree.unlock(p, PlayerClass.RIDDER, "taaiheid"));
		assertEquals(SkillTree.Result.UNKNOWN, tree.unlock(p, PlayerClass.RIDDER, "bestaat-niet"));
		assertEquals(3, tree.pointsLeft(p));
	}

	@Test
	void vaardigheidVanAndereKlasseIsGeblokkeerd() {
		PlayerProgress p = new PlayerProgress(5, 0);
		assertEquals(SkillTree.Result.WRONG_CLASS, tree.unlock(p, PlayerClass.RIDDER, "vuurbal"));
	}
}
