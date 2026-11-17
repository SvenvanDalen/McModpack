package com.drakenrijken.test;

import com.drakenrijken.data.RunewardenBoss;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

public class RunewardenGameTest {
	@GameTest
	public void wachterWordtGemaaktEnOpgeruimd(GameTestHelper context) {
		BlockPos plek = context.absolutePos(new BlockPos(2, 2, 2));
		if (!RunewardenBoss.spawn(context.getLevel(), plek)) throw new AssertionError("wachter niet gemaakt");
		if (!RunewardenBoss.active()) throw new AssertionError("wachter niet actief");
		if (RunewardenBoss.spawn(context.getLevel(), plek)) throw new AssertionError("tweede wachter mag niet");
		RunewardenBoss.stop();
		if (RunewardenBoss.active()) throw new AssertionError("wachter niet opgeruimd");
		context.succeed();
	}
}
