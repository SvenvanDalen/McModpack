package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class JapaneseWeaponsTest {
	@Test
	void alleVierWapensBestaan() {
		Set<String> ids = JapaneseWeapons.all().stream().map(JapaneseWeapons.Stats::id).collect(Collectors.toSet());
		assertEquals(Set.of("katana", "naginata", "yumi", "shuriken"), ids);
		assertEquals(4, JapaneseWeapons.all().size());
	}

	@Test
	void waardenZijnPositief() {
		for (JapaneseWeapons.Stats s : JapaneseWeapons.all()) {
			assertTrue(s.damage() > 0, s.id());
			assertTrue(s.reach() > 0, s.id());
			assertTrue(s.durability() > 0, s.id());
		}
	}

	@Test
	void katanaIsSterkerDanIjzerzwaard() {
		assertTrue(JapaneseWeapons.get("katana").damage() > 6);
		assertEquals(8, JapaneseWeapons.get("naginata").damage());
		assertEquals(3, JapaneseWeapons.get("naginata").reach());
		assertEquals(3, JapaneseWeapons.get("shuriken").damage());
	}

	@Test
	void shurikenIsVoorIedereen() {
		for (PlayerClass c : PlayerClass.values()) {
			assertTrue(JapaneseWeapons.usableBy("shuriken", c), c.name());
			assertTrue(JapaneseWeapons.usableBy("yumi", c), c.name());
		}
	}

	@Test
	void katanaEnNaginataAlleenVoorSamoerai() {
		for (PlayerClass c : PlayerClass.values()) {
			assertEquals(c == PlayerClass.SAMOERAI, JapaneseWeapons.usableBy("katana", c));
			assertEquals(c == PlayerClass.SAMOERAI, JapaneseWeapons.usableBy("naginata", c));
		}
		assertEquals(16, JapaneseWeapons.SHURIKEN_STACK);
	}
}
