package com.drakenrijken.test;

import com.drakenrijken.data.FastTravelService;
import com.drakenrijken.data.ModData;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ReisGameTest {
	@GameTest
	public void reisKostSmaragdenEnVerplaatst(GameTestHelper context) {
		var p = context.makeMockServerPlayerInLevel();
		var level = context.getLevel();
		var d = ModData.of(p);
		int hx = p.getBlockX(), hz = p.getBlockZ();
		d.discovered().add(hx + "," + hz);
		d.discovered().add((hx + 1000) + "," + hz);
		p.setAttached(ModData.PLAYER_DATA, d);
		String arm = FastTravelService.travel(level, p, 2);
		if (!arm.startsWith("Te weinig")) throw new AssertionError("verwacht te weinig smaragden: " + arm);
		p.getInventory().add(new ItemStack(Items.EMERALD, 20));
		String ok = FastTravelService.travel(level, p, 2);
		if (!ok.startsWith("Reis voltooid")) throw new AssertionError("reis mislukt: " + ok);
		if (p.getBlockX() < hx + 900) throw new AssertionError("niet verplaatst: " + p.getBlockX());
		context.succeed();
	}
}
