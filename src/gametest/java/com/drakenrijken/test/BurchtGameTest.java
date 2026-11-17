package com.drakenrijken.test;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.data.StructureBuilder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class BurchtGameTest {
	@GameTest
	public void burchtStaatInDeWereld(GameTestHelper context) {
		BlockPos hoek = context.absolutePos(new BlockPos(0, 8, 0));
		var level = context.getLevel();
		StructureBuilder.place(level, hoek, Blueprints.castle());
		if (!level.getBlockState(hoek.offset(10, 1, 15)).is(Blocks.GOLD_BLOCK)) throw new AssertionError("troon ontbreekt");
		if (!level.getBlockState(hoek.offset(10, -4, 14)).is(Blocks.CHEST)) throw new AssertionError("kerkerkist ontbreekt");
		if (!level.getBlockState(hoek.offset(8, -2, 9)).isAir()) throw new AssertionError("schacht naar de kerker is dicht");
		context.succeed();
	}
}
