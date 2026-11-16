package com.drakenrijken.test;

import com.drakenrijken.data.ModBlocks;
import com.drakenrijken.data.WaterWheelBlock;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;

public class WaterradGameTest {
	@GameTest
	public void krachtVolgtStromendWater(GameTestHelper context) {
		BlockPos rad = new BlockPos(2, 2, 2);
		context.setBlock(rad, ModBlocks.WATERRAD);
		var level = context.getLevel();
		BlockPos abs = context.absolutePos(rad);
		if (WaterWheelBlock.power(level, abs) != 0) throw new AssertionError("kracht zonder water");
		context.setBlock(rad.east(), Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1));
		if (WaterWheelBlock.power(level, abs) != 2) throw new AssertionError("verwacht 2 bij 1 zijde, kreeg " + WaterWheelBlock.power(level, abs));
		context.setBlock(rad.west(), Blocks.WATER.defaultBlockState());
		if (WaterWheelBlock.power(level, abs) != 2) throw new AssertionError("bronwater telt niet mee");
		context.succeed();
	}
}
