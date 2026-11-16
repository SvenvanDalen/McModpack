package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProgressionTest {
	@Test
	void xpForLevelIs100TimesLevel() {
		assertEquals(100, Progression.xpForLevel(1));
		assertEquals(200, Progression.xpForLevel(2));
	}

	@Test
	void nieuweSpelerStartOpLevel1MetNulXp() {
		PlayerProgress p = new PlayerProgress();
		assertEquals(1, p.level());
		assertEquals(0, p.xp());
	}

	@Test
	void tweehonderdVijftigXpOpLevel2GeeftLevel3MetRest() {
		PlayerProgress p = new PlayerProgress(2, 0);
		Progression.addXp(p, 250);
		assertEquals(3, p.level());
		assertEquals(50, p.xp());
	}

	@Test
	void eenGrootXpBedragKanMeerdereLevelsOverslaan() {
		PlayerProgress p = new PlayerProgress();
		Progression.addXp(p, 100 + 200 + 300 + 10);
		assertEquals(4, p.level());
		assertEquals(10, p.xp());
	}

	@Test
	void xpLooptNietOverBijMaxLevel() {
		PlayerProgress p = new PlayerProgress();
		Progression.addXp(p, Integer.MAX_VALUE);
		assertEquals(Progression.MAX_LEVEL, p.level());
		assertEquals(0, p.xp());
		Progression.addXp(p, 500);
		assertEquals(Progression.MAX_LEVEL, p.level());
		assertEquals(0, p.xp());
	}

	@Test
	void negatiefOfNulXpVeranderNiets() {
		PlayerProgress p = new PlayerProgress(5, 40);
		Progression.addXp(p, -100);
		Progression.addXp(p, 0);
		assertEquals(5, p.level());
		assertEquals(40, p.xp());
	}

	@Test
	void ongeldigeWaardenWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> Progression.xpForLevel(0));
		assertThrows(IllegalArgumentException.class, () -> new PlayerProgress(31, 0));
		assertThrows(IllegalArgumentException.class, () -> new PlayerProgress(1, -1));
	}
}
