package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SkillCostsTest {
	@Test
	void kostenAlleenBijGeslaagdGebruik() {
		var p = new PlayerProgress();
		var bar = new ResourceBar(30);
		assertEquals(SkillCosts.Result.NOT_UNLOCKED, SkillCosts.use(p, bar, "vuurbal"));
		p.unlocked().add("vuurbal");
		assertEquals(SkillCosts.Result.OK, SkillCosts.use(p, bar, "vuurbal"));
		assertEquals(10, bar.value());
		assertEquals(SkillCosts.Result.TOO_TIRED, SkillCosts.use(p, bar, "vuurbal"));
		assertEquals(10, bar.value());
		assertEquals(SkillCosts.Result.PASSIVE, SkillCosts.use(p, bar, "taaiheid"));
	}

	@Test
	void manaRegenVersnelt() {
		var p = new PlayerProgress();
		assertEquals(5, SkillCosts.regenPerSecond(p));
		p.unlocked().add("mana_regen");
		assertEquals(10, SkillCosts.regenPerSecond(p));
	}

	@Test
	void taaiheidGeeftExtraLeven() {
		var p = new PlayerProgress();
		assertEquals(0, SkillCosts.bonusHealth(p));
		p.unlocked().add("taaiheid");
		assertEquals(4, SkillCosts.bonusHealth(p));
	}

	@Test
	void snelheidEnZwaardbeheersing() {
		var p = new PlayerProgress();
		assertEquals(0.0, SkillCosts.bonusSpeed(p));
		assertEquals(0.0, SkillCosts.bonusDamage(p));
		p.unlocked().add("snelheid");
		p.unlocked().add("zwaardbeheersing");
		assertEquals(0.02, SkillCosts.bonusSpeed(p));
		assertEquals(2.0, SkillCosts.bonusDamage(p));
	}
}
