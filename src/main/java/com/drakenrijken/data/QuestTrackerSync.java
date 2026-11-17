package com.drakenrijken.data;

import com.drakenrijken.core.QuestTracker;
import com.drakenrijken.quest.QuestEvents;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

/** Stuurt elke seconde de tracker-regel naar de speler, maar alleen als die veranderd is. Uitzetten kan met /dr tracker (tot de server stopt). */
public final class QuestTrackerSync {
	private static final Map<UUID, String> LAST = new HashMap<>();
	private static final Set<UUID> OFF = new HashSet<>();

	private QuestTrackerSync() {}

	public static void init() {
		PayloadTypeRegistry.clientboundPlay().register(TrackerPayload.TYPE, TrackerPayload.CODEC);
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 20 != 0) return;
			for (ServerPlayer p : server.getPlayerList().getPlayers()) push(p);
		});
	}

	public static String textFor(ServerPlayer p) {
		if (OFF.contains(p.getUUID())) return "";
		return QuestTracker.line(QuestEvents.engine().quests(), ModData.of(p).quests());
	}

	private static void push(ServerPlayer p) {
		String t = textFor(p);
		if (t.equals(LAST.get(p.getUUID()))) return;
		LAST.put(p.getUUID(), t);
		ServerPlayNetworking.send(p, new TrackerPayload(t));
	}

	/** Zet de tracker aan of uit; geeft true als hij nu aan staat. */
	public static boolean toggle(ServerPlayer p) {
		boolean nuAan = OFF.remove(p.getUUID());
		if (!nuAan) OFF.add(p.getUUID());
		push(p);
		return nuAan;
	}
}
