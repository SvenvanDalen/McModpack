package com.drakenrijken.test;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.data.StructureBuilder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;

public class BewakerGameTest {
	@GameTest
	public void dungeonKrijgtBewakers(GameTestHelper context) {
		BlockPos hoek = context.absolutePos(new BlockPos(0, 2, 0));
		var level = context.getLevel();
		StructureBuilder.place(level, hoek, Blueprints.dungeon());
		int n = StructureBuilder.spawnGuards(level, hoek, Blueprints.dungeon(), "dungeon", 1);
		if (n != 2) throw new AssertionError("verwacht 2 bewakers, kreeg " + n);
		context.succeed();
	}
}
