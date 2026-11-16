package com.drakenrijken.test;

import com.drakenrijken.data.AxleNetwork;
import com.drakenrijken.data.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;

public class AssenGameTest {
	@GameTest
	public void assenGevenKrachtDoor(GameTestHelper context) {
		BlockPos rad = new BlockPos(2, 2, 2);
		BlockPos machine = new BlockPos(6, 2, 2);
		context.setBlock(rad, ModBlocks.WATERRAD);
		context.setBlock(rad.north(), Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1));
		context.setBlock(machine, ModBlocks.SMEDERIJ);
		var level = context.getLevel();
		BlockPos abs = context.absolutePos(machine);
		if (AxleNetwork.scan(level, abs).available() != 0) throw new AssertionError("kracht zonder assen");
		for (int x = 3; x <= 5; x++) context.setBlock(new BlockPos(x, 2, 2), ModBlocks.AS);
		var net = AxleNetwork.scan(level, abs);
		if (net.available() != 2) throw new AssertionError("verwacht 2, kreeg " + net.available());
		if (!net.canSupply(2) || net.canSupply(3)) throw new AssertionError("vraag klopt niet");
		context.succeed();
	}
}
