package com.drakenrijken.test;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.data.StructureBuilder;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class DorpGameTest {
	@GameTest
	public void dorpStaatInDeWereld(GameTestHelper context) {
		BlockPos hoek = context.absolutePos(new BlockPos(0, 2, 0));
		var level = context.getLevel();
		StructureBuilder.place(level, hoek, Blueprints.village());
		if (!level.getBlockState(hoek.offset(26, 1, 10)).is(Blocks.ANVIL)) throw new AssertionError("aambeeld ontbreekt");
		if (!level.getBlockState(hoek.offset(0, 0, 4)).is(Blocks.OAK_PLANKS)) throw new AssertionError("huismuur ontbreekt");
		if (!level.getBlockState(hoek.offset(3, 2, 7)).isAir()) throw new AssertionError("huis is niet hol");
		context.succeed();
	}
}
