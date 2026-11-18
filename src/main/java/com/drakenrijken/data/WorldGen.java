package com.drakenrijken.data;

import com.drakenrijken.core.Blueprints;
import com.drakenrijken.core.KingdomPlacement;
import com.drakenrijken.core.StructureQueue;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Geleidelijke wereldgeneratie (DR-306): bij het laden van een chunk met een koninkrijk (deterministisch uit de seed)
 * komt een dorp in de wachtrij; per tick wordt er hooguit één gebouwd, alleen als de omliggende chunks al geladen zijn.
 * Staat standaard uit (instelling generateStructures in drakenrijken.json).
 */
public final class WorldGen {
	private static final int MAX_TRIES = 200;
	private static final StructureQueue QUEUE = new StructureQueue();
	private static final ArrayDeque<int[]> RETRY = new ArrayDeque<>();

	private WorldGen() {}

	public static void init() {
		ServerChunkEvents.CHUNK_LOAD.register((level, chunk, fresh) -> {
			if (!com.drakenrijken.DrakenRijken.config.generateStructures() || level.dimension() != Level.OVERWORLD) return;
			int cx = chunk.getPos().x(), cz = chunk.getPos().z();
			if (KingdomPlacement.hasKingdom(level.getSeed(), cx, cz)) QUEUE.enqueue(new StructureQueue.Request("dorp", cx, cz));
		});
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (!com.drakenrijken.DrakenRijken.config.generateStructures()) return;
			ServerLevel level = server.overworld();
			for (var r : QUEUE.nextTick()) RETRY.add(new int[] {r.chunkX(), r.chunkZ(), 0});
			int n = RETRY.size();
			for (int i = 0; i < n; i++) {
				int[] t = RETRY.poll();
				if (build(level, t[0], t[1])) break;
				if (++t[2] < MAX_TRIES) RETRY.add(t);
			}
		});
	}

	/** Bouwt het dorp (en soms een burcht, dungeon of drakengebied ernaast) als de hele omgeving geladen is; false als dat nog niet kan. */
	public static boolean build(ServerLevel level, int cx, int cz) {
		int x = cx * 16, z = cz * 16;
		if (com.drakenrijken.core.CityPlans.isCity(level.getSeed(), cx, cz)) return buildCity(level, x, z);
		boolean extraNodig = !KingdomPlacement.companion(level.getSeed(), cx, cz).isEmpty();
		if (!level.hasChunksAt(x - 16, z - 16, x + (extraNodig ? 96 : 48), z + 48)) return false;
		int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x + 8, z + 8);
		if (!vlak(level, x, z, y)) return true; // water of steile helling: niet bouwen
		StructureBuilder.place(level, new BlockPos(x, y, z), Blueprints.village());
		String extra = KingdomPlacement.companion(level.getSeed(), cx, cz);
		if (!extra.isEmpty()) {
			int ex = x + 48, ez = z;
			int ey = level.getHeight(Heightmap.Types.WORLD_SURFACE, ex + 8, ez + 8);
			if (!vlak(level, ex, ez, ey)) return true;
			var plan = switch (extra) {
				case "burcht" -> Blueprints.castle();
				case "dungeon" -> Blueprints.dungeon();
				default -> Blueprints.lair();
			};
			var corner = new BlockPos(ex, extra.equals("burcht") ? ey + 6 : ey, ez);
			StructureBuilder.placeWithLoot(level, corner, plan, extra.equals("drakengebied") ? "ruine" : extra.equals("burcht") ? "burcht" : "dungeon", 1);
			StructureBuilder.spawnGuards(level, corner, plan, extra.equals("drakengebied") ? "drakengebied" : "dungeon", 1);
				if (extra.equals("drakengebied")) placeRuin(level, ex, ez);
		}
		return true;
	}
	/**
	 * Bouwt een ruïne (12x12) in de strook tussen dorp en drakengebied. Het verhaal hangt aan de plek van de steen (storyFor),
	 * dus we schuiven de hoek tot het verhaal bij het bouwplan past. Geeft de plek van de steen terug.
	 */
	public static BlockPos placeRuin(ServerLevel level, int ex, int ez) {
		for (int d = 0; d < 64; d++) {
			for (String story : com.drakenrijken.core.RuinPlans.STORIES) {
				var plan = com.drakenrijken.core.RuinPlans.plan(story);
				int[] s = com.drakenrijken.core.RuinPlans.stoneAt(plan);
				int cx = ex - 16 + d % 4, cz = ez + 4 + d / 4;
				int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, cx + 6, cz + 6);
				BlockPos stone = new BlockPos(cx + s[0], y, cz + s[1]);
				if (!RuinStoneBlock.storyFor(level.dimension().identifier() + "|" + stone.asLong()).equals(story)) continue;
				StructureBuilder.placeWithLoot(level, new BlockPos(cx, y, cz), plan, "ruine", 1);
				com.drakenrijken.DrakenRijken.LOGGER.info("Ruïne {} gebouwd, steen op {} {} {}", story, stone.getX(), stone.getY(), stone.getZ());
				return stone;
			}
		}
		return placeRuinStone(level, ex, ez);
	}

	/** Ruïnesteen met aanwijzingen vlak voor het drakengebied; geeft de plek terug. */
	public static BlockPos placeRuinStone(ServerLevel level, int ex, int ez) {
		int sx = ex - 2, sz = ez + 8;
		BlockPos p = new BlockPos(sx, level.getHeight(Heightmap.Types.WORLD_SURFACE, sx, sz), sz);
		level.setBlock(p, ModBlocks.RUINESTEEN.defaultBlockState(), 3);
		return p;
	}

	/** Grote stad (64x64, 200 inwoners) in een koninkrijk zonder burcht, dungeon of drakengebied. */
	static boolean buildCity(ServerLevel level, int x, int z) {
		if (!level.hasChunksAt(x - 16, z - 16, x + 80, z + 80)) return false;
		int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x + 32, z + 32);
		if (y != level.getHeight(Heightmap.Types.OCEAN_FLOOR, x + 32, z + 32)) return true;
		for (int[] h : new int[][] {{0, 0}, {63, 0}, {0, 63}, {63, 63}}) {
			if (Math.abs(level.getHeight(Heightmap.Types.WORLD_SURFACE, x + h[0], z + h[1]) - y) > 6) return true;
		}
		var plan = com.drakenrijken.core.CityPlans.city();
		StructureBuilder.place(level, new BlockPos(x, y, z), plan);
		com.drakenrijken.DrakenRijken.LOGGER.info("Grote stad gebouwd op {} {} {} met {} inwoners", x, y, z, com.drakenrijken.core.CityPlans.inhabitants(plan));
		return true;
	}

	/** Geen water in het midden en geen hoogteverschil van meer dan 6 blokken tussen midden en de vier hoeken van 32x32. */
	static boolean vlak(ServerLevel level, int x, int z, int y) {
		if (y != level.getHeight(Heightmap.Types.OCEAN_FLOOR, x + 8, z + 8)) return false;
		int[][] hoeken = {{0, 0}, {31, 0}, {0, 31}, {31, 31}};
		for (int[] h : hoeken) {
			int hy = level.getHeight(Heightmap.Types.WORLD_SURFACE, x + h[0], z + h[1]);
			if (Math.abs(hy - y) > 6) return false;
		}
		return true;
	}
}
