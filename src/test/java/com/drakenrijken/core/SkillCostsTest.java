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
}
