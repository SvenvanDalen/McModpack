package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.MachineRecipe;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Alle meegeleverde machinerecepten laden, hebben unieke id's en een bekende machine (DR-411). */
class BundledRecipesTest {
	@Test
	void receptenKloppen() throws IOException, URISyntaxException {
		Path dir = Paths.get(BundledRecipesTest.class.getResource("/data/drakenrijken/recipes").toURI());
		List<String> teksten = new ArrayList<>();
		try (Stream<Path> s = Files.list(dir)) {
			for (Path p : s.filter(f -> f.toString().endsWith(".json")).toList()) teksten.add(Files.readString(p));
		}
		List<String> warns = new ArrayList<>();
		List<MachineRecipe> recepten = MachineRecipeLoader.load(teksten.stream(), warns::add);
		assertTrue(warns.isEmpty(), warns.toString());
		assertEquals(teksten.size(), recepten.size());
		Set<String> ids = new HashSet<>();
		Set<String> machines = new HashSet<>();
		for (MachineRecipe r : recepten) {
			assertTrue(ids.add(r.id()), "dubbel id " + r.id());
			machines.add(r.machine());
		}
		assertEquals(Set.of("smederij", "zaag", "pers", "runesmederij"), machines);
		assertEquals(9, recepten.size());
	}
}
