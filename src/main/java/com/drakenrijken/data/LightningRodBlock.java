package com.drakenrijken.data;

import com.drakenrijken.core.LightningRod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Bliksemafleider met accu (#151). De lading zit in het blok zelf (0 tot 5 standen van 100), dus er is geen opslag nodig.
 * Bij onweer kan hij door een willekeurige tik getroffen worden; een machine in de buurt kan een stand gebruiken voor extra kracht.
 */
public class LightningRodBlock extends Block {
	public static final IntegerProperty LADING = IntegerProperty.create("lading", 0, LightningRod.CAPACITY / LightningRod.CHARGE_PER_STRIKE);
	/** Extra kracht die een stand lading een machine geeft voor één taak. */
	public static final int BOOST = 4;
	public static final int RADIUS = 8;

	public LightningRodBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(LADING, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(LADING); }

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.isThundering() && level.canSeeSky(pos.above())) strike(level, pos);
	}

	/** Een bliksemslag op deze afleider (alleen het licht-effect, zonder brand); geeft de lading die erbij kwam. */
	public static int strike(ServerLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (!state.is(ModBlocks.BLIKSEMAFLEIDER)) return 0;
		var bolt = EntityTypes.LIGHTNING_BOLT.create(level, net.minecraft.world.entity.EntitySpawnReason.EVENT);
		if (bolt != null) {
			bolt.setVisualOnly(true);
			bolt.snapTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
			level.addFreshEntity(bolt);
		}
		LightningRod rod = new LightningRod(state.getValue(LADING) * LightningRod.CHARGE_PER_STRIKE);
		int erbij = rod.strike();
		level.setBlock(pos, state.setValue(LADING, rod.charge() / LightningRod.CHARGE_PER_STRIKE), Block.UPDATE_ALL);
		return erbij;
	}

	/** Neemt één stand lading uit een afleider binnen {@link #RADIUS} blokken van de machine; true als dat gelukt is. */
	public static boolean drawNear(ServerLevel level, BlockPos machine) {
		for (BlockPos p : BlockPos.betweenClosed(machine.offset(-RADIUS, -RADIUS, -RADIUS), machine.offset(RADIUS, RADIUS, RADIUS))) {
			BlockState s = level.getBlockState(p);
			if (!s.is(ModBlocks.BLIKSEMAFLEIDER)) continue;
			LightningRod rod = new LightningRod(s.getValue(LADING) * LightningRod.CHARGE_PER_STRIKE);
			if (rod.draw(LightningRod.CHARGE_PER_STRIKE)) {
				level.setBlock(p.immutable(), s.setValue(LADING, rod.charge() / LightningRod.CHARGE_PER_STRIKE), Block.UPDATE_ALL);
				return true;
			}
		}
		return false;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		player.sendOverlayMessage(Component.literal("Lading: " + state.getValue(LADING) + "/" + (LightningRod.CAPACITY / LightningRod.CHARGE_PER_STRIKE) + " (laadt bij onweer)"));
		return InteractionResult.SUCCESS_SERVER;
	}
}
