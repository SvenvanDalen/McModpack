package com.drakenrijken.test;

import com.drakenrijken.data.ModData;
import com.drakenrijken.data.ModEra;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class TijdperkGameTest {
	@GameTest
	public void poortenOpenenTijdperken(GameTestHelper context) {
		var p = context.makeMockServerPlayerInLevel();
		if (ModEra.of(p) != 1) throw new AssertionError("start niet in tijdperk 1");
		ModData.of(p).quests().completed().add("poort_stoom");
		if (ModEra.of(p) != 2) throw new AssertionError("verwacht tijdperk 2");
		ModData.of(p).quests().completed().add("poort_runen");
		if (ModEra.of(p) != 3) throw new AssertionError("verwacht tijdperk 3");
		context.succeed();
	}
}
