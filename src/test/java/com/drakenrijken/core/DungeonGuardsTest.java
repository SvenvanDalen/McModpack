package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.data.SpawnTableLoader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DungeonGuardsTest {
	@Test
	void dungeonHeeftTweeBewakersBinnenDeKamers() {
		var plek = Blueprints.dungeon().spots().stream().filter(s -> s.role().equals("bewaker")).toList();
		assertEquals(2, plek.size());
		for (var s : plek) assertTrue(s.x() >= 9 && s.x() <= 24 && s.z() >= 1 && s.z() <= 5);
	}

	@Test
	void spawnlijstLaadtEnGeeftAltijdIets() throws Exception {
		try (var in = getClass().getResourceAsStream("/data/drakenrijken/spawns/dungeon.json")) {
			var tabel = SpawnTableLoader.load(new String(in.readAllBytes(), StandardCharsets.UTF_8));
			var r = new Random(1);
			for (int era = 1; era <= 3; era++) assertTrue(tabel.pick(era, r).isPresent());
		}
	}

	@Test
	void drakengebiedHeeftBewakersEnEigenLijst() throws Exception {
		assertEquals(2, Blueprints.lair().spots().stream().filter(s -> s.role().equals("bewaker")).count());
		try (var in = getClass().getResourceAsStream("/data/drakenrijken/spawns/drakengebied.json")) {
			var tabel = SpawnTableLoader.load(new String(in.readAllBytes(), StandardCharsets.UTF_8));
			for (int era = 1; era <= 3; era++) assertTrue(tabel.pick(era, new Random(2)).isPresent());
		}
	}
}
