package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.data.SpawnTableLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class SpawnTableTest {
	private static String bestand(String naam) throws Exception {
		try (var in = SpawnTableTest.class.getResourceAsStream("/data/drakenrijken/spawns/" + naam)) {
			return new String(in.readAllBytes());
		}
	}

	private static Set<String> trek(SpawnTable t, int era) {
		Random r = new Random(1);
		Set<String> mobs = new HashSet<>();
		for (int i = 0; i < 2000; i++) t.pick(era, r).ifPresent(e -> mobs.add(e.mob()));
		return mobs;
	}

	@Test
	void tijdperkEenGeeftGeenLatereVijanden() throws Exception {
		SpawnTable bergen = SpawnTableLoader.load(bestand("bergen.json"));
		Set<String> mobs = trek(bergen, 1);
		assertEquals(Set.of("minecraft:skeleton"), mobs);
	}

	@Test
	void laterTijdperkKanAllesGeven() throws Exception {
		assertEquals(3, trek(SpawnTableLoader.load(bestand("bergen.json")), 3).size());
		assertEquals(3, trek(SpawnTableLoader.load(bestand("bos.json")), 3).size());
	}

	@Test
	void gebiedenVerschillen() throws Exception {
		Set<String> bos = trek(SpawnTableLoader.load(bestand("bos.json")), 3);
		Set<String> bergen = trek(SpawnTableLoader.load(bestand("bergen.json")), 3);
		Set<String> gemeen = new HashSet<>(bos);
		gemeen.retainAll(bergen);
		assertTrue(gemeen.isEmpty());
	}

	@Test
	void legeMogelijkhedenGevenLeeg() {
		SpawnTable t = new SpawnTable("x", List.of(new SpawnTable.Entry("a", 1, 3)));
		assertTrue(t.pick(1, new Random(1)).isEmpty());
		assertTrue(new SpawnTable("x", List.of()).pick(3, new Random(1)).isEmpty());
	}

	@Test
	void ongeldigeWaardenWordenAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> new SpawnTable.Entry("", 1, 1));
		assertThrows(IllegalArgumentException.class, () -> new SpawnTable.Entry("x", 0, 1));
		assertThrows(IllegalArgumentException.class, () -> new SpawnTable.Entry("x", 1, 4));
		assertThrows(IllegalArgumentException.class, () -> new SpawnTable(" ", List.of()));
		assertThrows(IllegalArgumentException.class, () -> new SpawnTable("x", List.of()).pick(0, new Random()));
		assertThrows(IllegalArgumentException.class, () -> SpawnTableLoader.load("{}"));
	}

	@Test
	void loadAllSlaatKapotEnDubbelOver() throws Exception {
		List<String> meldingen = new ArrayList<>();
		String bos = bestand("bos.json");
		Map<String, SpawnTable> alle = SpawnTableLoader.loadAll(
			Stream.of(bos, "{kapot", bos, bestand("bergen.json")), meldingen::add);
		assertEquals(Set.of("bos", "bergen"), alle.keySet());
		assertEquals(2, meldingen.size());
		assertFalse(meldingen.isEmpty());
	}
}
