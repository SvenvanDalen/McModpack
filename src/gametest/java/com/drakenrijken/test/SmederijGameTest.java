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

	@GameTest
	public void bliksemafleiderLaadtOpEnGeeftKracht(GameTestHelper context) {
		var level = context.getLevel();
		var pos = context.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
		level.setBlock(pos, com.drakenrijken.data.ModBlocks.BLIKSEMAFLEIDER.defaultBlockState(), 3);
		if (com.drakenrijken.data.LightningRodBlock.drawNear(level, pos)) throw new AssertionError("lege afleider geeft kracht");
		for (int i = 0; i < 7; i++) com.drakenrijken.data.LightningRodBlock.strike(level, pos);
		int lading = level.getBlockState(pos).getValue(com.drakenrijken.data.LightningRodBlock.LADING);
		if (lading != 5) throw new AssertionError("lading hoort 5 te zijn, is " + lading);
		if (!com.drakenrijken.data.LightningRodBlock.drawNear(level, pos)) throw new AssertionError("geladen afleider geeft niets");
		if (level.getBlockState(pos).getValue(com.drakenrijken.data.LightningRodBlock.LADING) != 4) throw new AssertionError("lading niet verlaagd");
		context.succeed();
	}

	@GameTest
	public void geruchtentafelVertelNieuweGeruchten(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		String eerste = com.drakenrijken.data.RumorTableBlock.listen(player, 0.0);
		if (!eerste.startsWith("Gerucht: ")) throw new AssertionError("geen gerucht: " + eerste);
		if (com.drakenrijken.data.ModData.of(player).heard().size() != 1) throw new AssertionError("gerucht niet onthouden");
		String tweede = com.drakenrijken.data.RumorTableBlock.listen(player, 0.0);
		if (tweede.equals(eerste)) throw new AssertionError("zelfde gerucht twee keer");
		context.succeed();
	}

	@GameTest
	public void ruinesteenOnthultAanwijzingenEnBeloont(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		String a = com.drakenrijken.data.RuinStoneBlock.read(player, "test|3");
		if (!a.startsWith("Aanwijzing 1/3")) throw new AssertionError("eerste: " + a);
		com.drakenrijken.data.RuinStoneBlock.read(player, "test|3");
		String c = com.drakenrijken.data.RuinStoneBlock.read(player, "test|3");
		if (!c.contains("goudstaven")) throw new AssertionError("geen beloning: " + c);
		if (player.getInventory().countItem(Items.GOLD_INGOT) != 8) throw new AssertionError("verkeerde beloning");
		String d = com.drakenrijken.data.RuinStoneBlock.read(player, "test|3");
		if (!d.contains("al ontcijferd") || player.getInventory().countItem(Items.GOLD_INGOT) != 8) throw new AssertionError("dubbel: " + d);
		context.succeed();
	}

	@GameTest
	public void machinebordToontStatusEnStoringen(GameTestHelper context) {
		var level = context.getLevel();
		var bord = context.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
		var smederij = context.absolutePos(new net.minecraft.core.BlockPos(2, 2, 1));
		com.drakenrijken.data.MachineRuntime.clearForTest();
		var leeg = com.drakenrijken.data.MachineBoardBlock.report(level, bord);
		if (leeg.size() != 1 || !leeg.get(0).startsWith("Geen machines")) throw new AssertionError("leeg bord: " + leeg);
		level.setBlock(smederij, com.drakenrijken.data.ModBlocks.SMEDERIJ.defaultBlockState(), 3);
		var wacht = com.drakenrijken.data.MachineBoardBlock.report(level, bord);
		if (wacht.size() != 1 || !wacht.get(0).startsWith("smederij") || !wacht.get(0).contains("Wacht")) throw new AssertionError("wacht: " + wacht);
		com.drakenrijken.data.MachineRuntime.breakForTest(level, smederij);
		var kapot = com.drakenrijken.data.MachineBoardBlock.report(level, bord);
		if (!kapot.get(0).contains("Storing")) throw new AssertionError("storing: " + kapot);
		com.drakenrijken.data.MachineRuntime.clearForTest();
		context.succeed();
	}

	@GameTest
	public void glazenBuisMeetDeLeiding(GameTestHelper context) {
		var level = context.getLevel();
		var start = context.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
		for (int i = 0; i < 3; i++) level.setBlock(start.east(i), com.drakenrijken.data.ModBlocks.GLAZENBUIS.defaultBlockState(), 3);
		int l = com.drakenrijken.data.GlassPipeBlock.length(level, start);
		if (l != 3) throw new AssertionError("lengte hoort 3 te zijn, is " + l);
		if (!com.drakenrijken.data.GlassPipeBlock.describe(l).contains("doorvoer")) throw new AssertionError("geen doorvoer");
		if (!com.drakenrijken.data.GlassPipeBlock.describe(com.drakenrijken.core.Pipe.MAX_LENGTH + 1).contains("te lang")) throw new AssertionError("te lang niet gemeld");
		context.succeed();
	}

	@GameTest
	public void ruinesteenKomtBijDrakengebied(GameTestHelper context) {
		var level = context.getLevel();
		var pos = com.drakenrijken.data.WorldGen.placeRuinStone(level, context.absolutePos(new net.minecraft.core.BlockPos(5, 0, 0)).getX(), context.absolutePos(new net.minecraft.core.BlockPos(5, 0, 0)).getZ());
		if (!level.getBlockState(pos).is(com.drakenrijken.data.ModBlocks.RUINESTEEN)) throw new AssertionError("geen ruinesteen op " + pos);
		context.succeed();
	}

	@GameTest
	public void karstationLeverstKolenNaDeRit(GameTestHelper context) {
		com.drakenrijken.data.CartStationBlock.clearForTest();
		var player = context.makeMockServerPlayerInLevel();
		var level = context.getLevel();
		var pos = context.absolutePos(new net.minecraft.core.BlockPos(2, 1, 2));
		String a = com.drakenrijken.data.CartStationBlock.collect(player, level, pos, 0L);
		if (!a.contains("rijdt weg")) throw new AssertionError("start: " + a);
		String b = com.drakenrijken.data.CartStationBlock.collect(player, level, pos, 10 * 20L);
		if (!b.contains("onderweg")) throw new AssertionError("te vroeg: " + b);
		String c = com.drakenrijken.data.CartStationBlock.collect(player, level, pos, 45 * 20L);
		if (player.getInventory().countItem(Items.COAL) != 8) throw new AssertionError("45 s = 2 ronden = 8 kolen: " + c);
		com.drakenrijken.data.CartStationBlock.clearForTest();
		context.succeed();
	}

	@GameTest
	public void treinstationVraagtOntdektKoninkrijkEnStoom(GameTestHelper context) {
		var player = context.makeMockServerPlayerInLevel();
		var level = context.getLevel();
		var pos = context.absolutePos(new net.minecraft.core.BlockPos(2, 1, 2));
		if (com.drakenrijken.data.TrainStationBlock.boilerPower(level, pos) != 0) throw new AssertionError("geen ketel = geen kracht");
		String a = com.drakenrijken.data.TrainStationBlock.ride(level, player, pos);
		if (!a.contains("ontdekt koninkrijk")) throw new AssertionError("zonder ontdekt koninkrijk: " + a);
		context.succeed();
	}
}
