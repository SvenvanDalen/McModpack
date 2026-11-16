package com.drakenrijken.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Runesteen (tijdperk 3): levert POWER kracht zolang er een amethistblok tegenaan ligt als focus.
 * Eerste inschatting van de balans; het ontwerp zegt er nog niets over.
 */
public class RuneStoneBlock extends Block {
	public static final int POWER = 12;

	public RuneStoneBlock(Properties properties) {
		super(properties);
	}

	public static int power(BlockGetter level, BlockPos pos) {
		for (Direction d : Direction.values()) {
			if (level.getBlockState(pos.relative(d)).is(Blocks.AMETHYST_BLOCK)) return POWER;
		}
		return 0;
	}
}
