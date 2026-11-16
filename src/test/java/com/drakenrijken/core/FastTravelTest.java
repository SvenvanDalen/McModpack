package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import org.junit.jupiter.api.Test;

class FastTravelTest {
	private final FastTravel ft = new FastTravel();

	FastTravelTest() {
		ft.register(new FastTravel.Village("a", 0, 0));
		ft.register(new FastTravel.Village("b", 1000, 500));
		ft.register(new FastTravel.Village("c", 10, 10));
	}

	@Test
	void prijsVolgtAfstandMetMinimum() {
		assertEquals(15, ft.cost("a", "b"));
		assertEquals(FastTravel.MIN_COST, ft.cost("a", "c"));
		assertEquals(ft.cost("a", "b"), ft.cost("b", "a"));
	}

	@Test
	void alleenOntdekteDorpenEnGenoegMunten() {
		Set<String> ontdekt = Set.of("a", "b");
		assertEquals(FastTravel.Result.OK, ft.check(ontdekt, "a", "b", 15));
		assertEquals(FastTravel.Result.TOO_POOR, ft.check(ontdekt, "a", "b", 14));
		assertEquals(FastTravel.Result.NOT_DISCOVERED, ft.check(ontdekt, "a", "c", 99));
		assertEquals(FastTravel.Result.SAME_PLACE, ft.check(ontdekt, "a", "a", 99));
		assertEquals(FastTravel.Result.UNKNOWN_VILLAGE, ft.check(ontdekt, "a", "zzz", 99));
	}

	@Test
	void ongeldigeInvoerWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> ft.register(new FastTravel.Village("a", 1, 1)));
		assertThrows(IllegalArgumentException.class, () -> ft.cost("a", "zzz"));
	}

	@Test
	void enormeAfstandLooptNietOver() {
		FastTravel f = new FastTravel();
		f.register(new FastTravel.Village("x", Integer.MIN_VALUE, Integer.MIN_VALUE));
		f.register(new FastTravel.Village("y", Integer.MAX_VALUE, Integer.MAX_VALUE));
		assertEquals(85899345, f.cost("x", "y"));
	}
}
