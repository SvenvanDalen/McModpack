package com.drakenrijken.data;

import com.drakenrijken.core.RealmPower;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * Telt de macht van het rijk (DR-502): elke machine rond een punt levert de kracht die zijn assen
 * hem geven. Alleen op verzoek, zodat het geen tickkosten heeft. Het aantal machines is begrensd door RealmPower.
 */
public final class RealmScan {
	public static final int RADIUS = 24;

	private RealmScan() {}

	public static int powerAround(Level level, BlockPos center) {
		List<Integer> machines = new ArrayList<>();
		for (BlockPos p : BlockPos.betweenClosed(center.offset(-RADIUS, -RADIUS, -RADIUS), center.offset(RADIUS, RADIUS, RADIUS))) {
			Block b = level.getBlockState(p).getBlock();
			if (b instanceof MachineBlock) {
				int kracht = AxleNetwork.scan(level, p.immutable()).available();
				if (kracht > 0) machines.add(kracht);
			}
		}
		return RealmPower.total(machines);
	}

	/** Muurblokken (muren, steenbakstenen, kasseien) op een ring rond het punt; alleen bij een aanval gemeten (#153). */
	public static int wallBlocks(Level level, BlockPos center) {
		int ring = 16;
		int n = 0;
		for (int dy = -2; dy <= 6; dy++) {
			for (int d = -ring; d <= ring; d++) {
				n += wallAt(level, center.offset(d, dy, -ring)) + wallAt(level, center.offset(d, dy, ring));
				if (d > -ring && d < ring) n += wallAt(level, center.offset(-ring, dy, d)) + wallAt(level, center.offset(ring, dy, d));
			}
		}
		return n;
	}

	private static int wallAt(Level level, BlockPos p) {
		var s = level.getBlockState(p);
		return s.is(net.minecraft.tags.BlockTags.WALLS) || s.is(net.minecraft.world.level.block.Blocks.STONE_BRICKS)
			|| s.is(net.minecraft.world.level.block.Blocks.COBBLESTONE) ? 1 : 0;
	}

	/** Zet er dorpelingen bij tot de grens van dit rijksniveau (DR-503); geeft het aantal nieuwe. */
	public static int topUpVillagers(ServerLevel level, BlockPos center, int realmLevel) {
		int existing = level.getEntitiesOfClass(Villager.class, new AABB(center).inflate(RADIUS)).size();
		int n = com.drakenrijken.core.RealmVillagers.toSpawn(realmLevel, existing);
		int gelukt = 0;
		for (int i = 0; i < n; i++) {
			BlockPos plek = center.offset(i % 4 - 2, 0, i / 4 - 2);
			if (net.minecraft.world.entity.EntityTypes.VILLAGER.spawn(level, plek, EntitySpawnReason.MOB_SUMMONED) != null) gelukt++;
		}
		return gelukt;
	}
}
