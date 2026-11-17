package com.drakenrijken.test;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.data.StructureBuilder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.Container;

public class BuitGameTest {
	@GameTest
	public void dungeonkistKrijktBuit(GameTestHelper context) {
		BlockPos hoek = context.absolutePos(new BlockPos(0, 2, 0));
		var level = context.getLevel();
		StructureBuilder.placeWithLoot(level, hoek, Blueprints.dungeon(), "dungeon", 1);
		if (!(level.getBlockEntity(hoek.offset(21, 1, 3)) instanceof Container kist)) throw new AssertionError("geen kist");
		boolean gevuld = false;
		for (int i = 0; i < kist.getContainerSize(); i++) if (!kist.getItem(i).isEmpty()) gevuld = true;
		if (!gevuld) throw new AssertionError("kist is leeg");
		context.succeed();
	}
}
