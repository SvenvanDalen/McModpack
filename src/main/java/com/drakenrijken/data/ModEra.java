package com.drakenrijken.data;

import com.drakenrijken.core.EraGate;
import net.minecraft.server.level.ServerPlayer;

/** Het tijdperk van een speler, volgens de poort-quests (DR-412). */
public final class ModEra {
	private static final EraGate GATE = new EraGate("poort_stoom", "poort_runen");

	private ModEra() {}

	public static int of(ServerPlayer player) {
		return GATE.currentEra(ModData.of(player).quests());
	}
}
