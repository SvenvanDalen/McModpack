package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldMapTest {
	private final WorldMap map = new WorldMap();

	WorldMapTest() {
		map.register(new WorldMap.Realm("noord", 0, -1000));
		map.register(new WorldMap.Realm("oost", 1000, 0));
		map.register(new WorldMap.Realm("zuidwest", -1000, 1000));
	}

	@Test
	void kaartTooptAlleenOntdekteKoninkrijken() {
		assertTrue(map.markers().isEmpty());
		assertEquals(List.of("noord"), map.visit(10, -1000 + 30));
		assertEquals(1, map.markers().size());
		assertEquals("noord", map.markers().get(0).id());
	}

	@Test
	void teVerwegBlijftOnbekendEnDubbelBezoekGeeftNietsNieuws() {
		assertTrue(map.visit(0, 0).isEmpty());
		map.visit(1000, 0);
		assertTrue(map.visit(1000, 0).isEmpty());
		assertEquals(1, map.discovered().size());
	}

	@Test
	void randVanDeStraalTelt() {
		assertEquals(List.of("oost"), map.visit(1000 + WorldMap.DISCOVER_RADIUS, 0));
		assertTrue(map.visit(1000 - WorldMap.DISCOVER_RADIUS - 1, 5000).isEmpty());
	}

	@Test
	void kompasWijstDeJuisteKant() {
		map.visit(0, -1000);
		map.visit(1000, 0);
		map.visit(-1000, 1000);
		assertEquals(WorldMap.Direction.N, map.compass(0, 0, "noord"));
		assertEquals(WorldMap.Direction.E, map.compass(0, 0, "oost"));
		assertEquals(WorldMap.Direction.SW, map.compass(0, 0, "zuidwest"));
		assertEquals(WorldMap.Direction.W, map.compass(2000, 0, "oost"));
	}

	@Test
	void kompasWeigertOnbekendOfNietOntdekt() {
		assertThrows(IllegalArgumentException.class, () -> map.compass(0, 0, "noord"));
		assertThrows(IllegalArgumentException.class, () -> map.compass(0, 0, "nergens"));
	}

	@Test
	void dubbelOfLeegKoninkrijkWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> map.register(new WorldMap.Realm("noord", 1, 1)));
		assertThrows(IllegalArgumentException.class, () -> new WorldMap.Realm(" ", 0, 0));
	}

	@Test
	void extremeAfstandLooptNietOver() {
		WorldMap m = new WorldMap();
		m.register(new WorldMap.Realm("ver", Integer.MAX_VALUE, Integer.MAX_VALUE));
		assertTrue(m.visit(Integer.MIN_VALUE, Integer.MIN_VALUE).isEmpty());
	}
}
