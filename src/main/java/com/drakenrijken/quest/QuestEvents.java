package com.drakenrijken.quest;

import com.drakenrijken.DrakenRijken;
import com.drakenrijken.core.QuestDef;
import com.drakenrijken.core.QuestEngine;
import com.drakenrijken.core.QuestEvent;
import com.drakenrijken.data.ModData;
import com.drakenrijken.data.QuestLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** Dunne laag die Minecraft-gebeurtenissen omzet naar QuestEvents voor de questmotor. */
public final class QuestEvents {
	private static QuestEngine engine = new QuestEngine(List.of());
	/** Laatste inventaristelling per speler en item, voor het zien van opgeraapte items. */
	private static final Map<UUID, Map<String, Integer>> INVENTORY = new HashMap<>();

	private QuestEvents() {}

	public static QuestEngine engine() { return engine; }

	public static void init() {
		engine = new QuestEngine(loadQuests());
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, be) -> {
			if (player instanceof ServerPlayer sp) {
				send(sp, new QuestEvent("break_block", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString()));
			}
		});
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (source.getEntity() instanceof ServerPlayer sp && !(entity instanceof ServerPlayer)) {
				send(sp, new QuestEvent("kill", BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()));
			}
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 20 != 0) return;
			for (ServerPlayer sp : server.getPlayerList().getPlayers()) {
				checkPickups(sp);
				checkReach(sp);
			}
		});
	}

	private static void send(ServerPlayer sp, QuestEvent e) {
		for (String id : engine.onEvent(e, ModData.of(sp))) {
			DrakenRijken.LOGGER.info("{} voltooide quest {}", sp.getName().getString(), id);
		}
	}

	/** Een item opgeraapt = de telling in de inventaris gaat omhoog sinds een seconde geleden. */
	private static void checkPickups(ServerPlayer sp) {
		Map<String, Integer> now = new HashMap<>();
		for (int i = 0; i < sp.getInventory().getContainerSize(); i++) {
			ItemStack s = sp.getInventory().getItem(i);
			if (!s.isEmpty()) now.merge(BuiltInRegistries.ITEM.getKey(s.getItem()).toString(), s.getCount(), Integer::sum);
		}
		Map<String, Integer> before = INVENTORY.put(sp.getUUID(), now);
		if (before == null) return;
		now.forEach((item, count) -> {
			int gained = count - before.getOrDefault(item, 0);
			for (int i = 0; i < gained; i++) send(sp, new QuestEvent("pickup", item));
		});
	}

	/** Reach-quests hebben als doel "x,y,z,straal"; staat de speler erbinnen, dan volgt een event met dat doel. */
	private static void checkReach(ServerPlayer sp) {
		for (QuestDef q : engine.quests()) {
			if (!q.eventType().equals("reach")) continue;
			String[] p = q.target().split(",");
			if (p.length != 4) continue;
			try {
				double dx = sp.getX() - Double.parseDouble(p[0]);
				double dy = sp.getY() - Double.parseDouble(p[1]);
				double dz = sp.getZ() - Double.parseDouble(p[2]);
				double r = Double.parseDouble(p[3]);
				if (dx * dx + dy * dy + dz * dz <= r * r) send(sp, new QuestEvent("reach", q.target()));
			} catch (NumberFormatException e) {
				DrakenRijken.LOGGER.warn("Quest {} heeft een ongeldig reach-doel: {}", q.id(), q.target());
			}
		}
	}

	private static List<QuestDef> loadQuests() {
		Path dir = FabricLoader.getInstance().getModContainer(DrakenRijken.MOD_ID)
			.flatMap(c -> c.findPath("data/drakenrijken/quests")).orElse(null);
		if (dir == null) return List.of();
		try (Stream<Path> files = Files.list(dir)) {
			return QuestLoader.load(files.filter(f -> f.toString().endsWith(".json")).sorted().map(f -> {
				try {
					return Files.readString(f);
				} catch (IOException e) {
					return "";
				}
			}), DrakenRijken.LOGGER::warn);
		} catch (IOException e) {
			DrakenRijken.LOGGER.warn("Quests niet te laden: {}", e.getMessage());
			return List.of();
		}
	}
}
