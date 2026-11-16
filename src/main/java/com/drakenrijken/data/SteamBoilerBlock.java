package com.drakenrijken.data;

import com.drakenrijken.core.SteamBoiler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Stoomketel (tijdperk 2), eenvoudige versie zonder eigen opslag: levert kracht (SteamBoiler.POWER)
 * zolang er een brandende oven onder staat en water boven het blok. De brandstof is dus die van de oven.
 */
public class SteamBoilerBlock extends Block {
	public SteamBoilerBlock(Properties properties) {
		super(properties);
	}

	public static int power(BlockGetter level, BlockPos pos) {
		var below = level.getBlockState(pos.below());
		boolean fire = below.is(Blocks.FURNACE) && below.getValue(BlockStateProperties.LIT);
		boolean water = level.getFluidState(pos.above()).is(FluidTags.WATER);
		return fire && water ? SteamBoiler.POWER : 0;
	}
}
