package com.drakenrijken.data;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

/** Transportband (DR-409): duwt voorwerpen en spelers die erop staan in de richting van de pijl. Heeft geen kracht nodig. */
public class ConveyorBlock extends Block {
	public static final double SPEED = 0.06;

	public ConveyorBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection());
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		Direction d = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Vec3 v = entity.getDeltaMovement();
		entity.setDeltaMovement(v.x + d.getStepX() * SPEED, v.y, v.z + d.getStepZ() * SPEED);
	}
}
