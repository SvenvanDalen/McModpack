package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.Dialogue;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** De meegeleverde gesprekken laden, hebben bereikbare knooppunten en eindigen altijd (DR-208). */
class BundledDialoguesTest {
	@Test
	void gesprekkenKloppen() throws IOException, URISyntaxException {
		Path dir = Paths.get(BundledDialoguesTest.class.getResource("/data/drakenrijken/dialogues").toURI());
		List<String> teksten = new ArrayList<>();
		try (Stream<Path> s = Files.list(dir)) {
			for (Path p : s.filter(f -> f.toString().endsWith(".json")).toList()) teksten.add(Files.readString(p));
		}
		List<String> warns = new ArrayList<>();
		List<Dialogue> gesprekken = DialogueLoader.load(teksten.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		assertEquals(5, gesprekken.size());
		for (Dialogue d : gesprekken) {
			Set<String> gezien = new HashSet<>();
			ArrayDeque<String> todo = new ArrayDeque<>(List.of(d.start()));
			boolean kanEindigen = false;
			while (!todo.isEmpty()) {
				String id = todo.pop();
				if (!gezien.add(id)) continue;
				for (Dialogue.Choice c : d.nodes().get(id).choices()) {
					if (c.next() == null) kanEindigen = true;
					else todo.push(c.next());
				}
			}
			assertEquals(d.nodes().keySet(), gezien, d.id() + ": onbereikbare knooppunten");
			assertTrue(kanEindigen, d.id() + " eindigt nooit");
		}
	}
}
