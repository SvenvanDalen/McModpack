package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class MachineRecipeLoaderTest {
	private static final String GOED = "{\"id\":\"a\",\"machine\":\"smederij\",\"inputs\":{\"erts\":2},\"output\":\"staaf\",\"count\":2,\"ticks\":100,\"power\":1}";
	private static final String NUL = "{\"id\":\"b\",\"machine\":\"m\",\"inputs\":{\"x\":0},\"output\":\"o\"}";

	@Test
	void leestEenGeldigRecept() {
		var r = MachineRecipeLoader.load(Stream.of(GOED), w -> {}).get(0);
		assertEquals(2, r.inputs().get("erts"));
		assertEquals(2, r.outputCount());
		assertEquals(100, r.ticks());
	}

	@Test
	void kapotBestandWordtOvergeslagenRestLaadt() {
		List<String> warns = new ArrayList<>();
		var r = MachineRecipeLoader.load(Stream.of("{kapot", NUL, GOED, GOED), warns::add);
		assertEquals(1, r.size());
		assertEquals(3, warns.size());
	}

	@Test
	void stoomReceptenLadenMetJuisteMachines() throws Exception {
		List<String> teksten = new ArrayList<>();
		for (String naam : List.of("ijzer_staaf", "plank_zaag", "staal_pers")) {
			try (var in = getClass().getResourceAsStream("/data/drakenrijken/recipes/" + naam + ".json")) {
				teksten.add(new String(in.readAllBytes()));
			}
		}
		var r = MachineRecipeLoader.load(teksten.stream(), w -> { throw new AssertionError(w); });
		assertEquals(List.of("smederij", "zaag", "pers"), r.stream().map(x -> x.machine()).toList());
	}

	@Test
	void meegeleverdReceptLaadt() throws Exception {
		try (var in = getClass().getResourceAsStream("/data/drakenrijken/recipes/ijzer_staaf.json")) {
			var r = MachineRecipeLoader.load(Stream.of(new String(in.readAllBytes())), w -> { throw new AssertionError(w); });
			assertEquals("smederij", r.get(0).machine());
		}
	}
}
