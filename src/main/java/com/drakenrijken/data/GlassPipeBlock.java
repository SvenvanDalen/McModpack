package com.drakenrijken.data;

import com.drakenrijken.core.Pipe;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Glazen buis (#150): rechtsklikken meet de leiding waar dit blok bij hoort (lengte en doorvoer, regels in core.Pipe). Alleen bij een klik. */
public class GlassPipeBlock extends Block {
	public GlassPipeBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (level instanceof ServerLevel sl) player.sendOverlayMessage(Component.literal(describe(length(sl, pos))));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Aantal verbonden buisblokken; telt door tot één boven de maximale lengte, zodat een enorme leiding niets kost. */
	public static int length(ServerLevel level, BlockPos start) {
		Set<BlockPos> seen = new HashSet<>();
		ArrayDeque<BlockPos> todo = new ArrayDeque<>();
		seen.add(start);
		todo.add(start);
		while (!todo.isEmpty() && seen.size() <= Pipe.MAX_LENGTH) {
			BlockPos p = todo.poll();
			for (Direction d : Direction.values()) {
				BlockPos n = p.relative(d);
				if (seen.size() > Pipe.MAX_LENGTH) break;
				if (!seen.contains(n) && level.getBlockState(n).getBlock() instanceof GlassPipeBlock) {
					seen.add(n);
					todo.add(n);
				}
			}
		}
		return seen.size();
	}

	public static String describe(int length) {
		if (!Pipe.validLength(length)) return "Leiding te lang (maximaal " + Pipe.MAX_LENGTH + " blokken).";
		return "Leiding van " + length + " blokken, doorvoer " + Pipe.delivered(length, Integer.MAX_VALUE, Integer.MAX_VALUE) + " per tick.";
	}
}
