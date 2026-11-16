package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChunkTickerTest {
	private static final ChunkTicker.Chunk A = new ChunkTicker.Chunk(0, 0);
	private static final ChunkTicker.Chunk B = new ChunkTicker.Chunk(5, -3);

	@Test
	void alleenMachinesInGeladenChunksTikken() {
		ChunkTicker t = new ChunkTicker();
		t.add("zaag", A);
		t.add("pers", B);
		t.add("boiler", A);
		assertEquals(List.of("zaag", "boiler"), t.tickable(Set.of(A)));
		assertEquals(List.of("pers"), t.tickable(Set.of(B)));
		assertEquals(List.of("zaag", "pers", "boiler"), t.tickable(Set.of(A, B)));
	}

	@Test
	void nietsGeladenBetekentNietsTikt() {
		ChunkTicker t = new ChunkTicker();
		t.add("zaag", A);
		assertTrue(t.tickable(Set.of()).isEmpty());
		assertTrue(new ChunkTicker().tickable(Set.of(A)).isEmpty());
	}

	@Test
	void verwijderdeMachineTiktNietMeer() {
		ChunkTicker t = new ChunkTicker();
		t.add("zaag", A);
		assertTrue(t.remove("zaag"));
		assertFalse(t.remove("zaag"));
		assertEquals(0, t.size());
		assertTrue(t.tickable(Set.of(A)).isEmpty());
	}

	@Test
	void ongeldigeInvoerWordtAfgewezen() {
		ChunkTicker t = new ChunkTicker();
		t.add("zaag", A);
		assertThrows(IllegalArgumentException.class, () -> t.add("zaag", B));
		assertThrows(IllegalArgumentException.class, () -> t.add(" ", A));
	}
}
