package com.drakenrijken.test;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.data.StructureBuilder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class DrakenGebiedGameTest {
	@GameTest
	public void grotStaatInDeWereld(GameTestHelper context) {
		BlockPos hoek = context.absolutePos(new BlockPos(0, 2, 0));
		var level = context.getLevel();
		StructureBuilder.place(level, hoek, Blueprints.lair());
		if (!level.getBlockState(hoek.offset(12, 1, 19)).is(Blocks.CHEST)) throw new AssertionError("nestkist ontbreekt");
		if (!level.getBlockState(hoek.offset(12, 0, 12)).is(Blocks.MAGMA_BLOCK)) throw new AssertionError("magma ontbreekt");
		if (!level.getBlockState(hoek.offset(12, 2, 2)).isAir()) throw new AssertionError("ingang is dicht");
		if (!level.getBlockState(hoek.offset(5, 7, 5)).isAir()) throw new AssertionError("grot is niet hol");
		context.succeed();
	}
}
