package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class XpRulesTest {
	@Test
	void mobXpIsHalveGezondheidMinstensEen() {
		assertEquals(10, XpRules.mobKill(20));
		assertEquals(1, XpRules.mobKill(1));
		assertEquals(1, XpRules.mobKill(0));
	}

	@Test
	void bouwenGeeftEenXpPerTienBlokken() {
		assertEquals(0, XpRules.buildXp(9));
		assertEquals(1, XpRules.buildXp(10));
		assertEquals(5, XpRules.buildXp(59));
		assertEquals(0, XpRules.buildXp(-5));
	}

	@Test
	void bouwTellerGeeftXpAlleenBijVolleTiental() {
		BuildCounter c = new BuildCounter();
		int total = 0;
		for (int i = 0; i < 25; i++) total += c.place();
		assertEquals(2, total);
		assertEquals(5, c.pending());
	}

	@Test
	void xpLooptNietOverBijMaxLevelViaRegels() {
		PlayerProgress p = new PlayerProgress(30, 0);
		XpRules.award(p, XpRules.mobKill(1000));
		assertEquals(30, p.level());
		assertEquals(0, p.xp());
	}

	@Test
	void questBeloningWordtGeteld() {
		PlayerProgress p = new PlayerProgress();
		XpRules.award(p, 150);
		assertEquals(2, p.level());
		assertEquals(50, p.xp());
	}
}
