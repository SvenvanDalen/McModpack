package com.drakenrijken.data;

import com.drakenrijken.core.WaterWheel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;

/** Het waterrad: de kracht hangt af van de zijden met stromend (niet-bron) water, zie core.WaterWheel. */
public class WaterWheelBlock extends Block {
	public WaterWheelBlock(Properties properties) {
		super(properties);
	}

	/** Aantal zijden (noord, zuid, oost, west) waar stromend water langs komt. */
	public static int flowingSides(BlockGetter level, BlockPos pos) {
		int n = 0;
		for (Direction d : Direction.Plane.HORIZONTAL) {
			var fluid = level.getFluidState(pos.relative(d));
			if (fluid.getType() == Fluids.FLOWING_WATER) n++;
		}
		return n;
	}

	public static int power(BlockGetter level, BlockPos pos) {
		return WaterWheel.power(flowingSides(level, pos));
	}
}
