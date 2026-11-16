package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.data.LootTableLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LootTableTest {
	private static LootTable meegeleverd() throws Exception {
		try (var in = LootTableTest.class.getResourceAsStream("/data/drakenrijken/loot/dungeon.json")) {
			return LootTableLoader.load(new String(in.readAllBytes()));
		}
	}

	private static Set<String> trek(LootTable t, int era) {
		Random r = new Random(1);
		Set<String> items = new HashSet<>();
		for (int i = 0; i < 2000; i++) t.roll(era, r).ifPresent(e -> items.add(e.item()));
		return items;
	}

	@Test
	void tijdperkEenGeeftGeenLatereItems() throws Exception {
		Set<String> items = trek(meegeleverd(), 1);
		assertTrue(items.contains("minecraft:bread"));
		assertFalse(items.contains("minecraft:diamond"));
		assertFalse(items.contains("minecraft:amethyst_shard"));
	}

	@Test
	void laterTijdperkKanAllesGeven() throws Exception {
		assertEquals(6, trek(meegeleverd(), 3).size());
	}

	@Test
	void legeMogelijkhedenGevenLeeg() {
		LootTable t = new LootTable(List.of(new LootTable.Entry("x", 1, 1, 3)));
		assertTrue(t.roll(1, new Random(1)).isEmpty());
		assertTrue(new LootTable(List.of()).roll(3, new Random(1)).isEmpty());
	}

	@Test
	void gewichtTeltMee() {
		LootTable t = new LootTable(List.of(new LootTable.Entry("vaak", 1, 99, 1), new LootTable.Entry("zelden", 1, 1, 1)));
		Random r = new Random(7);
		int vaak = 0;
		for (int i = 0; i < 1000; i++) if (t.roll(1, r).get().item().equals("vaak")) vaak++;
		assertTrue(vaak > 950);
	}

	@Test
	void ongeldigeWaardenWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new LootTable.Entry("x", 0, 1, 1));
		assertThrows(IllegalArgumentException.class, () -> new LootTable.Entry("x", 1, 0, 1));
		assertThrows(IllegalArgumentException.class, () -> new LootTable.Entry("x", 1, 1, 4));
		assertThrows(IllegalArgumentException.class, () -> new LootTable(List.of()).roll(0, new Random()));
		assertThrows(IllegalArgumentException.class, () -> LootTableLoader.load("{}"));
	}
}
