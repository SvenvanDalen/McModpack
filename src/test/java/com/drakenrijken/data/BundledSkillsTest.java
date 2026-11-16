package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.Skill;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Controleert de meegeleverde vaardigheden: 6 per klasse en geen enkele wordt stilletjes overgeslagen. */
class BundledSkillsTest {
	@Test
	void zesVaardighedenPerKlasseZonderWaarschuwingen() throws IOException, URISyntaxException {
		Path dir = Paths.get(BundledSkillsTest.class.getResource("/data/drakenrijken/skills").toURI());
		List<String> teksten = new ArrayList<>();
		try (Stream<Path> files = Files.list(dir)) {
			for (Path p : files.filter(f -> f.toString().endsWith(".json")).toList()) teksten.add(Files.readString(p));
		}
		List<String> warns = new ArrayList<>();
		List<Skill> skills = SkillLoader.load(teksten.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		for (PlayerClass pc : PlayerClass.values()) {
			assertEquals(6, skills.stream().filter(s -> s.playerClass() == pc).count(), "klasse " + pc);
		}
	}
}
