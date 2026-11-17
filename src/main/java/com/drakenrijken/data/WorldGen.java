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

	/** Bouwt het dorp als de hele omgeving geladen is; false als dat nog niet kan. */
	static boolean build(ServerLevel level, int cx, int cz) {
		int x = cx * 16, z = cz * 16;
		if (!level.hasChunksAt(x - 16, z - 16, x + 48, z + 48)) return false;
		int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x + 8, z + 8);
		StructureBuilder.place(level, new BlockPos(x, y, z), Blueprints.village());
		return true;
	}
}
