package com.drakenrijken.test;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.data.StructureBuilder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class DungeonGameTest {
	@GameTest
	public void dungeonStaatInDeWereld(GameTestHelper context) {
		BlockPos hoek = context.absolutePos(new BlockPos(0, 2, 0));
		var level = context.getLevel();
		StructureBuilder.place(level, hoek, Blueprints.dungeon());
		if (!level.getBlockState(hoek.offset(21, 1, 3)).is(Blocks.CHEST)) throw new AssertionError("schatkist ontbreekt");
		if (!level.getBlockState(hoek.offset(11, 0, 3)).is(Blocks.MAGMA_BLOCK)) throw new AssertionError("val ontbreekt");
		if (!level.getBlockState(hoek.offset(0, 0, 0)).is(Blocks.STONE_BRICKS)) throw new AssertionError("muur ontbreekt");
		if (!level.getBlockState(hoek.offset(3, 2, 3)).isAir()) throw new AssertionError("kamer is niet hol");
		context.succeed();
	}
}
