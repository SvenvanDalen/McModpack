package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.EraGate;
import com.drakenrijken.core.PlayerData;
import com.drakenrijken.core.QuestDef;
import com.drakenrijken.core.QuestEngine;
import com.drakenrijken.core.QuestEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** De meegeleverde poort-quests ontgrendelen tijdperk 2 en 3 in de juiste volgorde (DR-412). */
class EraGateQuestsTest {
	@Test
	void poortQuestsOntgrendelenTijdperken() throws IOException, URISyntaxException {
		Path dir = Paths.get(EraGateQuestsTest.class.getResource("/data/drakenrijken/quests").toURI());
		List<String> texts = new ArrayList<>();
		try (Stream<Path> s = Files.list(dir)) {
			for (Path p : s.filter(f -> f.toString().endsWith(".json")).toList()) texts.add(Files.readString(p));
		}
		List<String> warns = new ArrayList<>();
		List<QuestDef> defs = QuestLoader.load(texts.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());

		PlayerData d = PlayerData.empty();
		QuestEngine engine = new QuestEngine(defs);
		EraGate gate = new EraGate("poort_stoom", "poort_runen");
		assertEquals(1, gate.currentEra(d.quests()));
		String[][] akte1 = {{"break_block", "minecraft:oak_log", "5"}, {"break_block", "minecraft:stone", "20"}, {"pickup", "minecraft:iron_ingot", "5"}, {"kill", "minecraft:spider", "5"}, {"kill", "minecraft:skeleton", "5"}};
		for (String[] a : akte1) {
			assertEquals(1, gate.currentEra(d.quests()));
			for (int i = 0; i < Integer.parseInt(a[2]); i++) engine.onEvent(new QuestEvent(a[0], a[1]), d);
		}
		for (int i = 0; i < 10; i++) engine.onEvent(new QuestEvent("kill", "minecraft:zombie"), d);
		assertEquals(2, gate.currentEra(d.quests()));
		String[][] akte2 = {{"pickup", "minecraft:copper_ingot", "10"}, {"pickup", "minecraft:coal", "20"}, {"kill", "minecraft:creeper", "5"}, {"pickup", "minecraft:amethyst_shard", "6"}};
		for (String[] a : akte2) {
			assertEquals(2, gate.currentEra(d.quests()));
			for (int i = 0; i < Integer.parseInt(a[2]); i++) engine.onEvent(new QuestEvent(a[0], a[1]), d);
		}
		for (int i = 0; i < 8; i++) engine.onEvent(new QuestEvent("kill", "minecraft:blaze"), d);
		assertEquals(3, gate.currentEra(d.quests()));
	}
}
