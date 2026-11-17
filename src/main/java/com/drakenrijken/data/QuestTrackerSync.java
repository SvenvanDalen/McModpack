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
		PayloadTypeRegistry.clientboundPlay().register(SkillsPayload.TYPE, SkillsPayload.CODEC);
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 20 != 0) return;
			for (ServerPlayer p : server.getPlayerList().getPlayers()) {
				ResourceBar b = BARS.get(p.getUUID());
				if (b != null) b.regen(com.drakenrijken.core.SkillCosts.regenPerSecond(ModData.of(p).progress()));
				var hp = p.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
				double wens = 20 + com.drakenrijken.core.SkillCosts.bonusHealth(ModData.of(p).progress());
				if (hp != null && hp.getBaseValue() != wens) hp.setBaseValue(wens);
				var pr = ModData.of(p).progress();
				var sp = p.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
				double wensSnelheid = 0.1 + com.drakenrijken.core.SkillCosts.bonusSpeed(pr);
				if (sp != null && sp.getBaseValue() != wensSnelheid) sp.setBaseValue(wensSnelheid);
				var dm = p.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
				double wensSchade = 1.0 + com.drakenrijken.core.SkillCosts.bonusDamage(pr);
				if (dm != null && dm.getBaseValue() != wensSchade) dm.setBaseValue(wensSchade);
				var kb = p.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE);
				double wensKb = com.drakenrijken.core.SkillCosts.knockbackResistance(pr);
				if (kb != null && kb.getBaseValue() != wensKb) kb.setBaseValue(wensKb);
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
		pushSkills(p);
		String t = textFor(p);
		if (t.equals(LAST.get(p.getUUID()))) return;
		LAST.put(p.getUUID(), t);
		ServerPlayNetworking.send(p, new TrackerPayload(t));
	}

	private static final Map<UUID, String> LAST_SKILLS = new HashMap<>();

	private static void pushSkills(ServerPlayer p) {
		var d = ModData.of(p);
		if (d.playerClass().isEmpty()) return;
		var pr = d.progress();
		String s = ModSkills.tree().pointsLeft(pr) + ";" + String.join(",", new java.util.TreeSet<>(pr.unlocked()));
		if (s.equals(LAST_SKILLS.get(p.getUUID()))) return;
		LAST_SKILLS.put(p.getUUID(), s);
		ServerPlayNetworking.send(p, new SkillsPayload(s));
	}

	/** Zet de quest-regel aan of uit; geeft true als hij nu aan staat. */
	public static boolean toggle(ServerPlayer p) {
		boolean nuAan = OFF.remove(p.getUUID());
		if (!nuAan) OFF.add(p.getUUID());
		push(p);
		return nuAan;
	}
}
