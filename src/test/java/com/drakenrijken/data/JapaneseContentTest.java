package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.QuestDef;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Buit, bewoners en quests van het Japanse koninkrijk (DR-1207). */
class JapaneseContentTest {
	private static String lees(String pad) throws IOException {
		try (var in = JapaneseContentTest.class.getResourceAsStream(pad)) {
			assertNotNull(in, pad + " ontbreekt");
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	@Test
	void japansDorpHeeftBuittabelMetEten() throws IOException {
		String tekst = lees("/data/drakenrijken/loot/japans_dorp.json");
		assertTrue(tekst.contains("drakenrijken:onigiri"));
		assertTrue(LootTableLoader.load(tekst).roll(1, new java.util.Random(1)).isPresent());
	}

	@Test
	void japansKasteelHeeftKatana() throws IOException {
		String tekst = lees("/data/drakenrijken/loot/japans_kasteel.json");
		assertTrue(tekst.contains("drakenrijken:katana"));
		assertTrue(LootTableLoader.load(tekst).roll(1, new java.util.Random(1)).isPresent());
	}

	@Test
	void erIsMinstensEenJapanseQuest() throws IOException, URISyntaxException {
		Path dir = Paths.get(JapaneseContentTest.class.getResource("/data/drakenrijken/quests").toURI());
		List<String> teksten = new ArrayList<>();
		try (Stream<Path> s = Files.list(dir)) {
			for (Path p : s.filter(f -> f.toString().endsWith(".json")).toList()) teksten.add(Files.readString(p));
		}
		List<String> warns = new ArrayList<>();
		List<QuestDef> alle = QuestLoader.load(teksten.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		List<QuestDef> japans = alle.stream().filter(q -> q.id().startsWith("japan_")).toList();
		assertTrue(japans.size() >= 1);
		for (QuestDef q : japans) {
			for (String r : q.requires()) assertTrue(alle.stream().anyMatch(o -> o.id().equals(r)), q.id() + " requires " + r);
		}
	}

	@Test
	void spawnlijstJapansBestaat() throws IOException {
		var tabel = SpawnTableLoader.load(lees("/data/drakenrijken/spawns/japans.json"));
		assertTrue(tabel.pick(1, new java.util.Random(1)).isPresent());
	}

	@Test
	void senseiHeeftDialoog() throws IOException {
		List<String> warns = new ArrayList<>();
		var d = DialogueLoader.load(Stream.of(lees("/data/drakenrijken/dialogues/sensei.json")), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		assertEquals(1, d.size());
		assertEquals("sensei", d.get(0).id());
	}

	@Test
	void themaKiestBuitEnSpawnsVoorHetKasteel() {
		var j = com.drakenrijken.core.KingdomTheme.JAPANS;
		var w = com.drakenrijken.core.KingdomTheme.WESTERS;
		assertEquals("japans_kasteel", com.drakenrijken.core.KingdomTheme.lootFor(j, "burcht"));
		assertEquals("japans", com.drakenrijken.core.KingdomTheme.spawnsFor(j, "burcht"));
		assertEquals("burcht", com.drakenrijken.core.KingdomTheme.lootFor(w, "burcht"));
		assertEquals("dungeon", com.drakenrijken.core.KingdomTheme.spawnsFor(w, "burcht"));
		assertEquals("ruine", com.drakenrijken.core.KingdomTheme.lootFor(j, "drakengebied"));
		assertEquals("drakengebied", com.drakenrijken.core.KingdomTheme.spawnsFor(w, "drakengebied"));
		assertEquals("dungeon", com.drakenrijken.core.KingdomTheme.lootFor(j, "dungeon"));
	}
}
