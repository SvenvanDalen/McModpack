package com.drakenrijken.data;

import com.drakenrijken.core.HudLines;
import com.drakenrijken.core.QuestTracker;
import com.drakenrijken.core.RealmState;
import com.drakenrijken.core.ResourceBar;
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

/**
 * Stuurt elke seconde de HUD-regels (quest, mana of uithouding, rijk-niveau) naar de speler, maar alleen als ze veranderd zijn.
 * De quest-regel uitzetten kan met /dr tracker (tot de server stopt). De balk zit in het geheugen en herstelt 5 per seconde;
 * er is nog niets dat hem verbruikt.
 */
public final class QuestTrackerSync {
	private static final Map<UUID, String> LAST = new HashMap<>();
	private static final Map<UUID, ResourceBar> BARS = new HashMap<>();
	private static final Set<UUID> OFF = new HashSet<>();

	private QuestTrackerSync() {}

	public static void init() {
		PayloadTypeRegistry.clientboundPlay().register(TrackerPayload.TYPE, TrackerPayload.CODEC);
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 20 != 0) return;
			for (ServerPlayer p : server.getPlayerList().getPlayers()) {
				ResourceBar b = BARS.get(p.getUUID());
				if (b != null) b.regen(com.drakenrijken.core.SkillCosts.regenPerSecond(ModData.of(p).progress()));
				push(p);
			}
		});
	}

	public static ResourceBar barOf(ServerPlayer p, com.drakenrijken.core.PlayerClass klasse) {
		return BARS.computeIfAbsent(p.getUUID(), k -> ResourceBar.forClass(klasse));
	}

	public static String textFor(ServerPlayer p) {
		var d = ModData.of(p);
		String quest = OFF.contains(p.getUUID()) ? "" : QuestTracker.line(QuestEvents.engine().quests(), d.quests());
		var klasse = d.playerClass().orElse(null);
		ResourceBar bar = klasse == null ? null : BARS.computeIfAbsent(p.getUUID(), k -> ResourceBar.forClass(klasse));
		return HudLines.build(quest, klasse, bar, RealmState.levelFor(d.realmPower()));
	}

	private static void push(ServerPlayer p) {
		String t = textFor(p);
		if (t.equals(LAST.get(p.getUUID()))) return;
		LAST.put(p.getUUID(), t);
		ServerPlayNetworking.send(p, new TrackerPayload(t));
	}

	/** Zet de quest-regel aan of uit; geeft true als hij nu aan staat. */
	public static boolean toggle(ServerPlayer p) {
		boolean nuAan = OFF.remove(p.getUUID());
		if (!nuAan) OFF.add(p.getUUID());
		push(p);
		return nuAan;
	}
}
