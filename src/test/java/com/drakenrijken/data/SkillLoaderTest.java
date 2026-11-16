package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.Skill;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class SkillLoaderTest {
	private static final String TAAI = "{\"id\":\"taaiheid\",\"class\":\"RIDDER\"}";
	private static final String STOOT = "{\"id\":\"schildstoot\",\"class\":\"RIDDER\",\"requires\":[\"taaiheid\"]}";

	private final List<String> warnings = new ArrayList<>();

	private List<Skill> load(String... files) {
		return SkillLoader.load(Stream.of(files), warnings::add);
	}

	@Test
	void laadtGeldigeVaardigheden() {
		List<Skill> s = load(TAAI, STOOT);
		assertEquals(2, s.size());
		assertEquals(PlayerClass.RIDDER, s.get(0).playerClass());
		assertEquals(List.of("taaiheid"), s.get(1).requires());
		assertTrue(warnings.isEmpty());
	}

	@Test
	void ongeldigeBestandenWordenOvergeslagenMetLogregel() {
		List<Skill> s = load("{kapot", "{\"id\":\"x\"}", "{\"id\":\"y\",\"class\":\"DRAAK\"}", "[]", TAAI);
		assertEquals(1, s.size());
		assertEquals(4, warnings.size());
	}

	@Test
	void dubbeleIdEnOnbekendeVoorwaardeWordenOvergeslagen() {
		List<Skill> s = load(TAAI, TAAI, "{\"id\":\"z\",\"class\":\"RIDDER\",\"requires\":[\"bestaat-niet\"]}");
		assertEquals(1, s.size());
		assertEquals(2, warnings.size());
	}
}
