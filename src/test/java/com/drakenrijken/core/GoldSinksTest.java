package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GoldSinksTest {
	@Test
	void legPlanIsGratis() {
		assertEquals(0, GoldSinks.blueprintGold(new Blueprint()));
	}

	@Test
	void eenGoudPerTienBlokkenNaarBovenAfgerond() {
		Blueprint p = new Blueprint();
		p.fill(0, 0, 0, 2, 0, 3, "stone");
		assertEquals(2, GoldSinks.blueprintGold(p));
		p.set(5, 0, 0, "stone");
		assertEquals(2, GoldSinks.blueprintGold(p));
		p.set(6, 0, 0, "stone");
		assertEquals(2, GoldSinks.blueprintGold(p));
	}

	@Test
	void muurOnderhoudPerDag() {
		assertEquals(0, GoldSinks.wallUpkeepPerDay(0));
		assertEquals(0, GoldSinks.wallUpkeepPerDay(-5));
		assertEquals(1, GoldSinks.wallUpkeepPerDay(1));
		assertEquals(1, GoldSinks.wallUpkeepPerDay(20));
		assertEquals(2, GoldSinks.wallUpkeepPerDay(21));
	}
}
