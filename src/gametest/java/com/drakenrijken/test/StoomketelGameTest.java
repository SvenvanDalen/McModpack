package com.drakenrijken.test;

import com.drakenrijken.core.SteamBoiler;
import com.drakenrijken.data.AxleNetwork;
import com.drakenrijken.data.ModBlocks;
import com.drakenrijken.data.SteamBoilerBlock;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class StoomketelGameTest {
	@GameTest
	public void ketelNeedtOvenEnWater(GameTestHelper context) {
		BlockPos ketel = new BlockPos(2, 3, 2);
		context.setBlock(ketel, ModBlocks.STOOMKETEL);
		context.setBlock(ketel.below(), Blocks.FURNACE.defaultBlockState());
		var level = context.getLevel();
		BlockPos abs = context.absolutePos(ketel);
		context.setBlock(ketel.above(), Blocks.WATER.defaultBlockState());
		if (SteamBoilerBlock.power(level, abs) != 0) throw new AssertionError("kracht met koude oven");
		context.setBlock(ketel.below(), Blocks.FURNACE.defaultBlockState().setValue(BlockStateProperties.LIT, true));
		if (SteamBoilerBlock.power(level, abs) != SteamBoiler.POWER) throw new AssertionError("geen kracht met oven en water");
		context.setBlock(ketel.above(), Blocks.AIR.defaultBlockState());
		if (SteamBoilerBlock.power(level, abs) != 0) throw new AssertionError("kracht zonder water");
		context.setBlock(ketel.above(), Blocks.WATER.defaultBlockState());
		context.setBlock(ketel.east(), ModBlocks.AS);
		BlockPos machine = ketel.east().east();
		context.setBlock(machine, ModBlocks.SMEDERIJ);
		if (AxleNetwork.scan(level, context.absolutePos(machine)).available() != SteamBoiler.POWER) throw new AssertionError("as geeft ketelkracht niet door");
		context.succeed();
	}
}
