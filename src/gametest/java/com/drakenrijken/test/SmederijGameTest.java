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
}
