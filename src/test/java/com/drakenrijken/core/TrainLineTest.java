package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

class TrainLineTest {
	private static FastTravel reis() {
		var f = new FastTravel();
		f.register(new FastTravel.Village("a", 0, 0));
		f.register(new FastTravel.Village("b", 1000, 0));
		return f;
	}

	@Test
	void prijsIsDeHelftVanSnelreis() {
		var f = reis();
		assertEquals(10, f.cost("a", "b"));
		assertEquals(5, TrainLine.price(f, "a", "b"));
	}

	@Test
	void reistijdRondtNaarBovenAf() {
		assertEquals(0, TrainLine.travelSeconds(0));
		assertEquals(1, TrainLine.travelSeconds(1));
		assertEquals(125, TrainLine.travelSeconds(1000));
		assertEquals(0, TrainLine.travelSeconds(-5));
	}

	@Test
	void ritWordtGeweigerdZonderOntdektDorpKrachtOfGeld() {
		var f = reis();
		assertEquals(TrainLine.Result.NOT_DISCOVERED, TrainLine.check(f, Set.of("a"), "a", "b", 99, 6));
		assertEquals(TrainLine.Result.UNKNOWN_VILLAGE, TrainLine.check(f, Set.of("a"), "a", "x", 99, 6));
		assertEquals(TrainLine.Result.SAME_PLACE, TrainLine.check(f, Set.of("a"), "a", "a", 99, 6));
		assertEquals(TrainLine.Result.NO_POWER, TrainLine.check(f, Set.of("a", "b"), "a", "b", 99, TrainLine.MIN_POWER - 1));
		assertEquals(TrainLine.Result.TOO_POOR, TrainLine.check(f, Set.of("a", "b"), "a", "b", 4, 6));
		assertEquals(TrainLine.Result.OK, TrainLine.check(f, Set.of("a", "b"), "a", "b", 5, 6));
	}
}
