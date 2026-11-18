package com.drakenrijken.test;

import com.drakenrijken.data.MachineBlock;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SmederijGameTest {
	@GameTest
	public void smederijMaaktIjzer(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		var inv = player.getInventory();
		MachineBlock.process(player, "smederij", 2);
		if (inv.countItem(Items.IRON_INGOT) != 0) throw new AssertionError("ijzer zonder grondstoffen");
		inv.add(new ItemStack(Items.RAW_IRON, 1));
		inv.add(new ItemStack(Items.COAL, 1));
		MachineBlock.process(player, "smederij", 0);
		if (inv.countItem(Items.IRON_INGOT) != 0) throw new AssertionError("ijzer zonder kracht");
		MachineBlock.process(player, "smederij", 2);
		if (inv.countItem(Items.IRON_INGOT) != 1) throw new AssertionError("verwacht 1 ijzerstaaf, kreeg " + inv.countItem(Items.IRON_INGOT));
		if (inv.countItem(Items.RAW_IRON) != 0 || inv.countItem(Items.COAL) != 0) throw new AssertionError("invoer niet verbruikt");
		context.succeed();
	}

	@GameTest
	public void zaagEnPersVragenKracht(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		var inv = player.getInventory();
		inv.add(new ItemStack(Items.OAK_LOG, 1));
		MachineBlock.process(player, "zaag", 3);
		if (inv.countItem(Items.OAK_PLANKS) != 0) throw new AssertionError("zaag met te weinig kracht");
		MachineBlock.process(player, "zaag", 4);
		if (inv.countItem(Items.OAK_PLANKS) != 6) throw new AssertionError("verwacht 6 planken, kreeg " + inv.countItem(Items.OAK_PLANKS));
		inv.add(new ItemStack(Items.IRON_INGOT, 2));
		inv.add(new ItemStack(Items.COAL, 2));
		MachineBlock.process(player, "pers", 6);
		if (inv.countItem(Items.IRON_BLOCK) != 1) throw new AssertionError("pers maakte geen ijzerblok");
		context.succeed();
	}

	@GameTest
	public void machineLooptDoorEnGeeftGeenDubbeleUitvoer(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		var inv = player.getInventory();
		inv.add(new ItemStack(Items.RAW_IRON, 1));
		inv.add(new ItemStack(Items.COAL, 1));
		var pos = context.absolutePos(new net.minecraft.core.BlockPos(1, 1, 1));
		var level = context.getLevel();
		com.drakenrijken.data.MachineRuntime.clearForTest();
		com.drakenrijken.data.MachineRuntime.click(player, level, pos, "smederij", 2);
		if (!com.drakenrijken.data.MachineRuntime.running(level, pos)) throw new AssertionError("werk loopt niet");
		if (inv.countItem(Items.RAW_IRON) != 0) throw new AssertionError("invoer niet verbruikt");
		String tweede = com.drakenrijken.data.MachineRuntime.click(player, level, pos, "smederij", 2);
		if (!tweede.startsWith("Bezig")) throw new AssertionError("tweede klik zou voortgang moeten tonen: " + tweede);
		com.drakenrijken.data.MachineRuntime.clearForTest();
		context.succeed();
	}

	@GameTest
	public void belastingkistBetaaltGoud(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		String leeg = com.drakenrijken.data.TaxChestBlock.collect(player);
		if (player.getInventory().countItem(Items.GOLD_INGOT) != 0) throw new AssertionError("goud zonder rijk: " + leeg);
		var d = com.drakenrijken.data.ModData.of(player);
		d.setRealmPower(1000);
		d.setLastTaxDay(-3);
		player.setAttached(com.drakenrijken.data.ModData.PLAYER_DATA, d);
		com.drakenrijken.data.TaxChestBlock.collect(player);
		if (player.getInventory().countItem(Items.GOLD_INGOT) < 1) throw new AssertionError("geen belasting uitbetaald");
		int eerste = player.getInventory().countItem(Items.GOLD_INGOT);
		com.drakenrijken.data.TaxChestBlock.collect(player);
		if (player.getInventory().countItem(Items.GOLD_INGOT) != eerste) throw new AssertionError("dubbel uitbetaald");
		context.succeed();
	}

	@GameTest
	public void bouwplantafelZetWerkplaatsNeer(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		var level = context.getLevel();
		var corner = context.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
		String zonder = com.drakenrijken.data.BlueprintTableBlock.build(player, level, corner, com.drakenrijken.core.Blueprints.workshop());
		if (!zonder.startsWith("Je mist")) throw new AssertionError("zonder materiaal: " + zonder);
		player.getInventory().add(new ItemStack(com.drakenrijken.data.ModBlocks.WATERRAD.asItem()));
		player.getInventory().add(new ItemStack(com.drakenrijken.data.ModBlocks.AS.asItem()));
		player.getInventory().add(new ItemStack(com.drakenrijken.data.ModBlocks.SMEDERIJ.asItem()));
		String klaar = com.drakenrijken.data.BlueprintTableBlock.build(player, level, corner, com.drakenrijken.core.Blueprints.workshop());
		if (!klaar.startsWith("Gebouwd: 3")) throw new AssertionError(klaar);
		if (!level.getBlockState(corner.offset(2, 0, 0)).is(com.drakenrijken.data.ModBlocks.SMEDERIJ)) throw new AssertionError("smederij niet geplaatst");
		if (player.getInventory().countItem(com.drakenrijken.data.ModBlocks.AS.asItem()) != 0) throw new AssertionError("materiaal niet verbruikt");
		context.succeed();
	}

	@GameTest
	public void moersleutelHerstelKapotteMachine(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		var pos = context.absolutePos(new net.minecraft.core.BlockPos(1, 1, 1));
		var level = context.getLevel();
		com.drakenrijken.data.MachineRuntime.clearForTest();
		if (com.drakenrijken.data.MachineRuntime.repair(level, pos)) throw new AssertionError("niets te herstellen");
		com.drakenrijken.data.MachineRuntime.breakForTest(level, pos);
		String melding = com.drakenrijken.data.MachineRuntime.click(player, level, pos, "smederij", 2);
		if (!melding.startsWith("Storing")) throw new AssertionError("storing niet gemeld: " + melding);
		if (!com.drakenrijken.data.MachineRuntime.repair(level, pos)) throw new AssertionError("herstel mislukt");
		if (com.drakenrijken.data.MachineRuntime.broken(level, pos)) throw new AssertionError("nog kapot");
		com.drakenrijken.data.MachineRuntime.clearForTest();
		context.succeed();
	}
}
