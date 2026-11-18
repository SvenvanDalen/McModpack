package com.drakenrijken.data;

import com.drakenrijken.core.KingdomPlacement;
import com.drakenrijken.core.WorldMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/** Kaart en kompas (DR-307): spelers ontdekken koninkrijken door erbij in de buurt te komen; /dr kaart toont ze. */
public final class WorldDiscovery {
	private WorldDiscovery() {}

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 40 != 0 || !com.drakenrijken.DrakenRijken.config.buildsStructures()) return;
			for (ServerPlayer p : server.getPlayerList().getPlayers()) {
				if (p.level() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) visit(level, p);
			}
		});
	}

	/** Ontdekt koninkrijken rond de speler; geeft het aantal nieuwe. */
	public static int visit(ServerLevel level, ServerPlayer p) {
		var data = ModData.of(p);
		int nieuw = 0;
		for (int[] m : KingdomPlacement.near(level.getSeed(), p.getBlockX(), p.getBlockZ(), WorldMap.DISCOVER_RADIUS)) {
			if (data.discovered().add(m[0] + "," + m[1])) nieuw++;
		}
		if (nieuw > 0) {
			p.setAttached(ModData.PLAYER_DATA, data);
			p.sendSystemMessage(Component.literal("Je hebt een koninkrijk ontdekt! Zie /dr kaart."));
		}
		return nieuw;
	}

	/** Kaarttekst: elk ontdekt koninkrijk met coördinaten, afstand en kompasrichting. */
	public static String describe(ServerPlayer p) {
		var data = ModData.of(p);
		if (data.discovered().isEmpty()) return "Je hebt nog geen koninkrijken ontdekt.";
		var map = new WorldMap();
		for (String s : data.discovered()) {
			String[] c = s.split(",");
			int x = Integer.parseInt(c[0]), z = Integer.parseInt(c[1]);
			map.register(new WorldMap.Realm(s, x, z));
			map.visit(x, z);
		}
		var lines = new java.util.ArrayList<String>();
		int i = 1;
		for (var r : map.markers()) {
			long dx = r.x() - p.getBlockX(), dz = r.z() - p.getBlockZ();
			lines.add(i++ + ". x=" + r.x() + " z=" + r.z() + " (" + Math.round(Math.sqrt((double) dx * dx + dz * dz)) + " blokken, richting "
				+ map.compass(p.getBlockX(), p.getBlockZ(), r.id()) + ")");
		}
		return "Ontdekte koninkrijken:\n" + String.join("\n", lines);
	}
}
