package com.drakenrijken.data;

import com.drakenrijken.core.Season;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;

/** Meldt aan alle spelers wanneer het seizoen wisselt (#155). Eén getal per dag; geen invloed op weer of kleuren. */
public final class SeasonAnnouncer {
	private static Season last;

	private SeasonAnnouncer() {}

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 100 != 0) return;
			Season nu = Season.of(server.overworld().getGameTime() / 24000L);
			if (last != null && nu != last) {
				var tekst = Component.literal("Het seizoen verandert: het is nu " + nu.naam().toLowerCase() + ".");
				for (var p : server.getPlayerList().getPlayers()) p.sendSystemMessage(tekst);
			}
			last = nu;
		});
	}
}
