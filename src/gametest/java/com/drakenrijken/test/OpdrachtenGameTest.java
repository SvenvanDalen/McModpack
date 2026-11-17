package com.drakenrijken.test;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

/** Regressietest (DR-907): /dr wachter ontbrak ooit in het commando (DR-610c). */
public class OpdrachtenGameTest {
	@GameTest
	public void drCommandoHeeftAlleBaasOpdrachten(GameTestHelper context) {
		var root = context.getLevel().getServer().getCommands().getDispatcher().getRoot().getChild("dr");
		if (root == null) throw new AssertionError("/dr bestaat niet");
		for (String naam : new String[] {"draak", "wachter", "gebruik", "tracker", "belasting", "seizoen", "beroepen"}) {
			if (root.getChild(naam) == null) throw new AssertionError("/dr " + naam + " ontbreekt");
		}
		context.succeed();
	}
}
