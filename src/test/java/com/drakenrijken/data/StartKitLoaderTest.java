package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.StartKit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StartKitLoaderTest {
	private static final String RIDDER = "{\"class\":\"ridder\",\"items\":{\"iron_sword\":1,\"bread\":8}}";

	@Test
	void leestEenStartset() {
		List<String> warns = new ArrayList<>();
		List<StartKit> kits = StartKitLoader.load(Stream.of(RIDDER), warns::add);
		assertEquals(1, kits.size());
		assertEquals(Map.of("iron_sword", 1, "bread", 8), kits.get(0).items());
		assertTrue(warns.isEmpty());
	}

	@Test
	void kapotOfDubbelBestandWordtOvergeslagen() {
		List<String> warns = new ArrayList<>();
		List<StartKit> kits = StartKitLoader.load(Stream.of(
			"{kapot", RIDDER, RIDDER,
			"{\"class\":\"draak\",\"items\":{}}",
			"{\"class\":\"magier\",\"items\":{\"stick\":0}}",
			"{\"class\":\"schurk\",\"items\":{\"dagger\":1}}"), warns::add);
		assertEquals(2, kits.size());
		assertEquals(4, warns.size());
	}

	@Test
	void klasseZonderBestandKrijgtLegeSet() {
		List<StartKit> kits = StartKitLoader.load(Stream.of(RIDDER), w -> {});
		assertTrue(StartKit.forClass(kits, PlayerClass.MAGIER).items().isEmpty());
		assertEquals(2, StartKit.forClass(kits, PlayerClass.RIDDER).items().size());
	}
}
