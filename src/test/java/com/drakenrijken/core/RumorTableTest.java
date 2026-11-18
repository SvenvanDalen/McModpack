package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RumorTableTest {
	private final RumorTable table = new RumorTable(List.of(
		new RumorTable.Rumor("a", "Een oude toren.", "", "toren"),
		new RumorTable.Rumor("b", "Een draak in de bergen.", "toren", "berg")));

	@Test
	void voorwaardeMoetVervuldZijn() {
		assertEquals(1, table.available(Set.of(), Set.of()).size());
		assertEquals(2, table.available(Set.of("toren"), Set.of()).size());
	}

	@Test
	void geenHerhaling() {
		assertNull(table.pick(Set.of(), Set.of("a"), 0.5));
		assertEquals("b", table.pick(Set.of("toren"), Set.of("a"), 0.9).id());
	}

	@Test
	void worpKiestGeldigGerucht() {
		assertEquals("a", table.pick(Set.of("toren"), Set.of(), 0.0).id());
		assertEquals("b", table.pick(Set.of("toren"), Set.of(), 0.999).id());
		assertEquals("a", table.pick(Set.of("toren"), Set.of(), -3).id());
	}
}
