package com.drakenrijken.data;

import com.drakenrijken.core.PowerNetwork;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

/** Zoekt vanaf een machine de aaneengesloten assen en de waterraden ernaast, en maakt er een PowerNetwork van. */
public final class AxleNetwork {
	private AxleNetwork() {}

	public static PowerNetwork scan(BlockGetter level, BlockPos machine) {
		Set<BlockPos> seen = new HashSet<>();
		Set<BlockPos> wheels = new HashSet<>();
		ArrayDeque<BlockPos> todo = new ArrayDeque<>();
		int shafts = 0;
		todo.add(machine);
		seen.add(machine);
		while (!todo.isEmpty()) {
			BlockPos p = todo.poll();
			for (Direction d : Direction.values()) {
				BlockPos n = p.relative(d);
				var block = level.getBlockState(n).getBlock();
				if (block == ModBlocks.WATERRAD || block == ModBlocks.WINDMOLEN || block == ModBlocks.STOOMKETEL || block == ModBlocks.RUNESTEEN) {
					wheels.add(n);
				} else if (block == ModBlocks.AS && shafts < PowerNetwork.MAX_SHAFTS && seen.add(n)) {
					shafts++;
					todo.add(n);
				}
			}
		}
		List<Integer> sources = new ArrayList<>();
		for (BlockPos w : wheels) sources.add(sourcePower(level, w));
		return new PowerNetwork(sources, shafts);
	}

	private static int sourcePower(BlockGetter level, BlockPos pos) {
		var block = level.getBlockState(pos).getBlock();
		if (block == ModBlocks.STOOMKETEL) return SteamBoilerBlock.power(level, pos);
		if (block == ModBlocks.RUNESTEEN) return RuneStoneBlock.power(level, pos);
		if (block == ModBlocks.WINDMOLEN) return WindMillBlock.power(level, pos);
		return WaterWheelBlock.power(level, pos);
	}
}
