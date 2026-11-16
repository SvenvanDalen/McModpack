package com.drakenrijken.test;

import com.drakenrijken.data.DragonBoss;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

public class DraakGameTest {
	@GameTest
	public void draakWordtGemaaktEnOpgeruimd(GameTestHelper context) {
		BlockPos plek = context.absolutePos(new BlockPos(2, 2, 2));
		if (!DragonBoss.spawn(context.getLevel(), plek)) throw new AssertionError("draak niet gemaakt");
		if (!DragonBoss.active()) throw new AssertionError("draak niet actief");
		if (DragonBoss.spawn(context.getLevel(), plek)) throw new AssertionError("tweede draak mag niet");
		DragonBoss.stop();
		if (DragonBoss.active()) throw new AssertionError("draak niet opgeruimd");
		context.succeed();
	}

	@GameTest
	public void draakHerstelAlsNiemandVecht(GameTestHelper context) {
		BlockPos plek = context.absolutePos(new BlockPos(2, 2, 2));
		if (!DragonBoss.spawn(context.getLevel(), plek)) throw new AssertionError("draak niet gemaakt");
		DragonBoss.damageForTest(150f);
		DragonBoss.tick();
		if (DragonBoss.healthFraction() < 0.99f) throw new AssertionError("draak herstelde niet, fractie " + DragonBoss.healthFraction());
		if (DragonBoss.phase() != 1) throw new AssertionError("verwachtte fase 1, kreeg " + DragonBoss.phase());
		DragonBoss.stop();
		context.succeed();
	}
}
