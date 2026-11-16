package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.LootTable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Elke meegeleverde buittabel laadt, heeft tijdperk-1-buit en geeft nooit buit uit een hoger tijdperk (DR-309). */
class BundledLootTest {
	@Test
	void tabellenVolgenDeTijdperken() throws IOException, URISyntaxException {
		Path dir = Paths.get(BundledLootTest.class.getResource("/data/drakenrijken/loot").toURI());
		List<Path> files;
		try (Stream<Path> s = Files.list(dir)) {
			files = s.filter(f -> f.toString().endsWith(".json")).toList();
		}
		assertTrue(files.size() >= 3, "te weinig buittabellen");
		for (Path p : files) {
			LootTable table = LootTableLoader.load(Files.readString(p));
			Random random = new Random(1);
			for (int era = 1; era <= 3; era++) {
				for (int i = 0; i < 200; i++) {
					LootTable.Entry e = table.roll(era, random).orElseThrow(() -> new AssertionError(p + ": leeg"));
					assertTrue(e.minEra() <= era, p + " gaf tijdperk " + e.minEra() + " bij " + era);
				}
			}
			assertEquals(1, table.roll(1, random).orElseThrow().minEra(), p.toString());
		}
	}
}
