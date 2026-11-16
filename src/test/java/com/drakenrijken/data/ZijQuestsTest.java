package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Zes zijquests per tijdperk laden samen met de hoofdquests zonder waarschuwing (DR-211). */
class ZijQuestsTest {
	@Test
	void zesPerTijdperk() throws IOException, URISyntaxException {
		Path dir = Paths.get(ZijQuestsTest.class.getResource("/data/drakenrijken/quests").toURI());
		List<String> alle = new ArrayList<>();
		try (Stream<Path> s = Files.list(dir)) {
			for (Path p : s.filter(f -> f.toString().endsWith(".json")).toList()) alle.add(Files.readString(p));
		}
		List<String> warns = new ArrayList<>();
		var quests = QuestLoader.load(alle.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		for (int era = 1; era <= 3; era++) {
			String voorvoegsel = "zij" + era + "_";
			assertEquals(6, quests.stream().filter(q -> q.id().startsWith(voorvoegsel)).count(), voorvoegsel);
		}
	}
}
