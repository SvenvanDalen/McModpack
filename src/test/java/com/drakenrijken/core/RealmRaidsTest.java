package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RealmRaidsTest {
	@Test
	void geenAanvallenOnderNiveauTwee() {
		for (int l = 0; l < RealmRaids.FIRST_RAID_LEVEL; l++) {
			assertFalse(RealmRaids.raidsAt(l));
			assertEquals(0, RealmRaids.waveSize(l));
			assertEquals(0, RealmRaids.waves(l));
			assertEquals(0, RealmRaids.enemyEra(l));
		}
	}

	@Test
	void golvenWordenGroterEnNooitBovenDeGrens() {
		int vorige = 0;
		for (int l = RealmRaids.FIRST_RAID_LEVEL; l <= RealmState.MAX_LEVEL; l++) {
			int size = RealmRaids.waveSize(l);
			assertTrue(size > vorige, "niveau " + l);
			assertTrue(size <= RealmRaids.MAX_WAVE_SIZE);
			vorige = size;
		}
		assertEquals(3, RealmRaids.waveSize(2));
		assertEquals(9, RealmRaids.waveSize(5));
	}

	@Test
	void wavesEnTijdperk() {
		assertEquals(1, RealmRaids.waves(3));
		assertEquals(2, RealmRaids.waves(4));
		assertEquals(1, RealmRaids.enemyEra(2));
		assertEquals(2, RealmRaids.enemyEra(4));
		assertEquals(3, RealmRaids.enemyEra(5));
	}

	@Test
	void ongeldigNiveau() {
		assertThrows(IllegalArgumentException.class, () -> RealmRaids.waveSize(-1));
		assertThrows(IllegalArgumentException.class, () -> RealmRaids.raidsAt(6));
	}
}
