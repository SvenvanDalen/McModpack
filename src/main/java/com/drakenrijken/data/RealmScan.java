package com.drakenrijken.data;

import com.drakenrijken.core.RealmPower;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
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
}
