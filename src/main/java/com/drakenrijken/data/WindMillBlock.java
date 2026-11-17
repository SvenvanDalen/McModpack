package com.drakenrijken.data;

import com.drakenrijken.core.WindMill;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

/** De windmolen: kracht uit core.WindMill; vrije lucht = zicht op de hemel en lucht op de vier zijkanten. */
public class WindMillBlock extends Block {
	public WindMillBlock(Properties properties) {
		super(properties);
	}

	public static int power(BlockGetter level, BlockPos pos) {
		boolean free = true;
		for (Direction d : Direction.Plane.HORIZONTAL) {
			if (!level.getBlockState(pos.relative(d)).isAir()) free = false;
		}
		if (free && level instanceof LevelReader r && !r.canSeeSky(pos.above())) free = false;
		return WindMill.power(pos.getY(), free);
	}
}
