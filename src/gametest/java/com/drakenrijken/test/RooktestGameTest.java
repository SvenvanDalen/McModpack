package com.drakenrijken.test;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.gametest.framework.GameTestHelper;

public class RooktestGameTest {
	@GameTest
	public void modIsGeladen(GameTestHelper context) {
		if (!FabricLoader.getInstance().isModLoaded("drakenrijken")) {
			throw new AssertionError("mod drakenrijken is niet geladen");
		}
		context.succeed();
	}
}
