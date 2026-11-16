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

/** Akte 2 heeft 8 quests die samen met akte 1 zonder waarschuwing laden (DR-209). */
class Akte2Test {
	@Test
	void achtQuestsLadenInEenKeten() throws IOException, URISyntaxException {
		Path dir = Paths.get(Akte2Test.class.getResource("/data/drakenrijken/quests").toURI());
		List<String> alle = new ArrayList<>();
		int akte2 = 0;
		try (Stream<Path> s = Files.list(dir)) {
			for (Path p : s.filter(f -> f.toString().endsWith(".json")).toList()) {
				alle.add(Files.readString(p));
				if (p.getFileName().toString().startsWith("akte2_")) akte2++;
			}
		}
		assertEquals(8, akte2);
		List<String> warns = new ArrayList<>();
		var quests = QuestLoader.load(alle.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		assertEquals(16, quests.size());
		assertTrue(quests.stream().anyMatch(q -> q.id().equals("epiloog_verdrag")));
		assertTrue(quests.stream().anyMatch(q -> q.id().equals("epiloog_eigen_bewind")));
	}
}
