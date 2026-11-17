package com.drakenrijken.data;

import com.drakenrijken.core.Blueprint;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

/** Zet een bouwplan uit de core in de wereld. Onbekende blokken worden overgeslagen. */
public final class StructureBuilder {
	private StructureBuilder() {}

	/** Plaatst het plan met de hoek (0,0,0) op {@code corner}; geeft het aantal geplaatste blokken. */
	public static int place(ServerLevel level, BlockPos corner, Blueprint plan) {
		int n = 0;
		for (var p : plan.placements()) {
			var id = Identifier.tryParse(p.block());
			var block = id == null ? null : BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
			if (block == null) continue;
			level.setBlock(corner.offset(p.x(), p.y(), p.z()), block.defaultBlockState(), 2);
			n++;
		}
		return n;
	}

	/** Plaatst het plan en vult elke kist erin met buit uit de tabel {@code lootName} (bestand in data/drakenrijken/loot). */
	public static int placeWithLoot(ServerLevel level, BlockPos corner, Blueprint plan, String lootName, int era) {
		int n = place(level, corner, plan);
		var table = table(lootName);
		if (table == null) return n;
		var random = new java.util.Random(corner.asLong());
		for (var p : plan.placements()) {
			if (!p.block().equals("chest")) continue;
			if (level.getBlockEntity(corner.offset(p.x(), p.y(), p.z())) instanceof net.minecraft.world.Container chest) {
				int slots = chest.getContainerSize();
				for (int i = 0; i < 5; i++) {
					var e = table.roll(era, random);
					if (e.isEmpty()) continue;
					var id = Identifier.tryParse(e.get().item());
					var item = id == null ? null : BuiltInRegistries.ITEM.getOptional(id).orElse(null);
					if (item == null) continue;
					chest.setItem(random.nextInt(slots), new net.minecraft.world.item.ItemStack(item, e.get().count()));
				}
			}
		}
		return n;
	}

	/** Tijdperk (1 tot 3) van de speler, voor het kiezen van buit. */
	public static int eraOf(net.minecraft.server.level.ServerPlayer p) {
		return new com.drakenrijken.core.EraGate("poort_stoom", "poort_runen").currentEra(ModData.of(p).quests());
	}

	private static com.drakenrijken.core.LootTable table(String name) {
		try (var in = StructureBuilder.class.getResourceAsStream("/data/drakenrijken/loot/" + name + ".json")) {
			if (in == null) return null;
			return LootTableLoader.loadSafe(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8), s -> {}).orElse(null);
		} catch (java.io.IOException e) {
			return null;
		}
	}

	/** Zet op elke plek met rol "bewaker" een vijand uit de spawnlijst {@code spawnName}; houdt de vijandengrens aan. Geeft het aantal. */
	public static int spawnGuards(ServerLevel level, BlockPos corner, Blueprint plan, String spawnName, int era) {
		com.drakenrijken.core.SpawnTable table;
		try (var in = StructureBuilder.class.getResourceAsStream("/data/drakenrijken/spawns/" + spawnName + ".json")) {
			if (in == null) return 0;
			table = SpawnTableLoader.load(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
		} catch (java.io.IOException e) {
			return 0;
		}
		var random = new java.util.Random(corner.asLong());
		int bestaand = level.getEntitiesOfClass(net.minecraft.world.entity.monster.Monster.class,
			new net.minecraft.world.phys.AABB(corner).inflate(64)).size();
		int n = 0;
		for (var s : plan.spots()) {
			if (!s.role().equals("bewaker") || bestaand + n >= com.drakenrijken.core.EntityBudget.MAX_ENEMIES) continue;
			var keuze = table.pick(era, random);
			if (keuze.isEmpty()) continue;
			var id = Identifier.tryParse(keuze.get().mob());
			var type = id == null ? java.util.Optional.<net.minecraft.world.entity.EntityType<?>>empty() : BuiltInRegistries.ENTITY_TYPE.getOptional(id);
			if (type.isEmpty()) continue;
			var mob = type.get().spawn(level, corner.offset(s.x(), s.y(), s.z()), net.minecraft.world.entity.EntitySpawnReason.EVENT);
			if (mob != null) {
				if (mob instanceof net.minecraft.world.entity.Mob m) m.setPersistenceRequired();
				n++;
			}
		}
		return n;
	}
}
