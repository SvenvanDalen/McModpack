package com.drakenrijken.test;

import com.drakenrijken.data.AxleNetwork;
import com.drakenrijken.data.ModBlocks;
import com.drakenrijken.data.RuneStoneBlock;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class RunesteenGameTest {
	@GameTest
	public void runesteenNeedtAmethist(GameTestHelper context) {
		BlockPos steen = new BlockPos(2, 2, 2);
		context.setBlock(steen, ModBlocks.RUNESTEEN);
		var level = context.getLevel();
		if (RuneStoneBlock.power(level, context.absolutePos(steen)) != 0) throw new AssertionError("kracht zonder amethist");
		context.setBlock(steen.north(), Blocks.AMETHYST_BLOCK);
		if (RuneStoneBlock.power(level, context.absolutePos(steen)) != RuneStoneBlock.POWER) throw new AssertionError("geen kracht met amethist");
		context.setBlock(steen.east(), ModBlocks.AS);
		BlockPos machine = steen.east().east();
		context.setBlock(machine, ModBlocks.RUNESMEDERIJ);
		if (AxleNetwork.scan(level, context.absolutePos(machine)).available() != RuneStoneBlock.POWER) throw new AssertionError("as geeft runekracht niet door");
		context.succeed();
	}
}
