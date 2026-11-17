package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.data.LootTableLoader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BurchtLootTest {
	@Test
	void burchtHeeftEigenBuitMetSmaragden() throws Exception {
		LootTable t;
		try (var in = BurchtLootTest.class.getResourceAsStream("/data/drakenrijken/loot/burcht.json")) {
			t = LootTableLoader.load(new String(in.readAllBytes()));
		}
		Set<String> items = new HashSet<>();
		Random r = new Random(1);
		for (int i = 0; i < 2000; i++) t.roll(1, r).ifPresent(e -> items.add(e.item()));
		assertTrue(items.contains("minecraft:emerald"));
		assertTrue(!items.contains("minecraft:diamond"));
	}
}
