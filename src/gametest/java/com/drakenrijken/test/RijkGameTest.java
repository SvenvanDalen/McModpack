package com.drakenrijken.test;

import com.drakenrijken.core.RealmState;
import com.drakenrijken.data.ModBlocks;
import com.drakenrijken.data.RealmScan;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public class RijkGameTest {
	@GameTest
	public void machtUitMachines(GameTestHelper context) {
		var level = context.getLevel();
		BlockPos midden = context.absolutePos(new BlockPos(2, 2, 2));
		if (RealmScan.powerAround(level, midden) != 0) throw new AssertionError("macht zonder machines");
		BlockPos steen = new BlockPos(2, 2, 2);
		context.setBlock(steen, ModBlocks.RUNESTEEN);
		context.setBlock(steen.north(), Blocks.AMETHYST_BLOCK);
		context.setBlock(steen.east(), ModBlocks.AS);
		context.setBlock(steen.east().east(), ModBlocks.RUNESMEDERIJ);
		int macht = RealmScan.powerAround(level, midden);
		if (macht != 12) throw new AssertionError("verwachtte macht 12, kreeg " + macht);
		if (RealmState.levelFor(macht) != 1) throw new AssertionError("verwachtte niveau 1");
		context.succeed();
	}

	@GameTest
	public void dorpelingenGroeien(GameTestHelper context) {
		var level = context.getLevel();
		BlockPos midden = context.absolutePos(new BlockPos(2, 2, 2));
		int eerst = RealmScan.topUpVillagers(level, midden, 0);
		if (eerst != 4) throw new AssertionError("verwachtte 4 dorpelingen, kreeg " + eerst);
		int nog = RealmScan.topUpVillagers(level, midden, 0);
		if (nog != 0) throw new AssertionError("tweede keer moet 0 zijn, kreeg " + nog);
		context.succeed();
	}
}
