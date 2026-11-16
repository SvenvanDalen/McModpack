package com.drakenrijken.data;

import com.drakenrijken.core.EntityBudget;
import com.drakenrijken.core.RealmRaids;
import com.drakenrijken.core.RealmState;
import com.drakenrijken.core.SpawnTable;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

/** Vijandelijke aanvallen op het rijk (DR-505): elke 10 minuten per speler vanaf niveau 2, of met /dr aanval. */
public final class RealmRaid {
	public static final int INTERVAL_TICKS = 12000;
	private static final Random RANDOM = new Random();
	private static SpawnTable table;

	private RealmRaid() {}

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % INTERVAL_TICKS != 0) return;
			for (ServerPlayer sp : server.getPlayerList().getPlayers()) {
				if (sp.level() instanceof ServerLevel sl) launch(sl, sp, RealmState.levelFor(ModData.of(sp).realmPower()));
			}
		});
	}

	private static SpawnTable table() {
		if (table == null) {
			try (InputStream in = RealmRaid.class.getResourceAsStream("/data/drakenrijken/spawns/aanval.json")) {
				table = SpawnTableLoader.load(new String(in.readAllBytes(), StandardCharsets.UTF_8));
			} catch (Exception e) {
				throw new IllegalStateException("aanvalslijst niet te laden", e);
			}
		}
		return table;
	}

	/** Zet de aanvallers neer en geeft hun aantal terug; 0 onder niveau 2. Houdt de vijandengrens aan. */
	public static int launch(ServerLevel level, ServerPlayer player, int realmLevel) {
		if (!RealmRaids.raidsAt(realmLevel)) return 0;
		int wanted = RealmRaids.waveSize(realmLevel) * RealmRaids.waves(realmLevel);
		int bestaand = level.getEntitiesOfClass(Monster.class, new AABB(player.blockPosition()).inflate(RealmScan.RADIUS)).size();
		int ruimte = Math.max(0, EntityBudget.MAX_ENEMIES - bestaand);
		int era = RealmRaids.enemyEra(realmLevel);
		int gelukt = 0;
		for (int i = 0; i < Math.min(wanted, ruimte); i++) {
			var keuze = table().pick(era, RANDOM);
			if (keuze.isEmpty()) break;
			Identifier id = Identifier.tryParse(keuze.get().mob());
			var type = id == null ? java.util.Optional.<net.minecraft.world.entity.EntityType<?>>empty() : BuiltInRegistries.ENTITY_TYPE.getOptional(id);
			if (type.isEmpty()) continue;
			BlockPos plek = player.blockPosition().offset(RANDOM.nextInt(11) - 5 + (i % 2 == 0 ? 8 : -8), 0, RANDOM.nextInt(11) - 5);
			if (type.get().spawn(level, plek, EntitySpawnReason.EVENT) != null) gelukt++;
		}
		if (gelukt > 0) player.sendSystemMessage(Component.literal("Je rijk wordt aangevallen! " + gelukt + " vijanden naderen."));
		return gelukt;
	}
}
