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
		int basis = RealmScan.powerAround(level, midden); // buurtests kunnen machines laten staan
		BlockPos steen = new BlockPos(2, 2, 2);
		context.setBlock(steen, ModBlocks.RUNESTEEN);
		context.setBlock(steen.north(), Blocks.AMETHYST_BLOCK);
		context.setBlock(steen.east(), ModBlocks.AS);
		context.setBlock(steen.east().east(), ModBlocks.RUNESMEDERIJ);
		int macht = RealmScan.powerAround(level, midden);
		if (basis == 0 && macht != 12) throw new AssertionError("verwachtte macht 12, kreeg " + macht);
		if (macht <= basis) throw new AssertionError("macht niet gestegen");
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

	@GameTest
	public void handelaarWordtVriendelijker(GameTestHelper context) {
		var speler = context.makeMockServerPlayerInLevel();
		var dorpeling = net.minecraft.world.entity.EntityTypes.VILLAGER.spawn(
			context.getLevel(), context.absolutePos(new BlockPos(2, 2, 2)), net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED);
		if (dorpeling == null) throw new AssertionError("dorpeling niet gemaakt");
		com.drakenrijken.data.RealmTrading.apply(dorpeling, speler, 0);
		int laag = dorpeling.getPlayerReputation(speler);
		com.drakenrijken.data.RealmTrading.apply(dorpeling, speler, 5);
		int hoog = dorpeling.getPlayerReputation(speler);
		if (hoog <= laag) throw new AssertionError("reputatie steeg niet: " + laag + " -> " + hoog);
		com.drakenrijken.data.RealmTrading.apply(dorpeling, speler, 0);
		if (dorpeling.getPlayerReputation(speler) != laag) throw new AssertionError("reputatie niet teruggezet");
		context.succeed();
	}

	@GameTest
	public void aanvalVanafNiveauTwee(GameTestHelper context) {
		var speler = context.makeMockServerPlayerInLevel();
		var level = context.getLevel();
		if (com.drakenrijken.data.RealmRaid.launch(level, speler, 1) != 0) throw new AssertionError("aanval onder niveau 2");
		int n = com.drakenrijken.data.RealmRaid.launch(level, speler, 2);
		if (n < 1 || n > 3) throw new AssertionError("verwachtte 1 tot 3 aanvallers, kreeg " + n);
		context.succeed();
	}
}
