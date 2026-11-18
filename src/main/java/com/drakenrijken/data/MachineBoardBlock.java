package com.drakenrijken.data;

import com.drakenrijken.core.MachineOverview;
import com.drakenrijken.core.RecipeMachine;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Machinebord (#148): rechtsklikken toont de status van alle machines binnen 8 blokken, problemen eerst. Alleen bij een klik, nooit per tick. */
public class MachineBoardBlock extends Block {
	public static final int RADIUS = 8;

	public MachineBoardBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (level instanceof ServerLevel sl) {
			for (String regel : report(sl, pos)) player.sendSystemMessage(Component.literal(regel));
		}
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Regels voor het overzicht van de machines rond {@code centrum}. */
	public static List<String> report(ServerLevel level, BlockPos centrum) {
		List<MachineOverview.Entry> entries = new ArrayList<>();
		List<String> kapot = new ArrayList<>();
		for (BlockPos p : BlockPos.betweenClosed(centrum.offset(-RADIUS, -RADIUS, -RADIUS), centrum.offset(RADIUS, RADIUS, RADIUS))) {
			if (!(level.getBlockState(p).getBlock() instanceof MachineBlock m)) continue;
			BlockPos vast = p.immutable();
			String naam = m.machineName() + " (" + vast.getX() + "," + vast.getY() + "," + vast.getZ() + ")";
			if (MachineRuntime.broken(level, vast)) { kapot.add(naam + ": Storing! Herstel met een moersleutel"); continue; }
			int pct = MachineRuntime.percentAt(level, vast);
			entries.add(new MachineOverview.Entry(naam, pct >= 0 ? RecipeMachine.State.RUNNING : RecipeMachine.State.IDLE, Math.max(0, pct) / 100.0));
		}
		List<String> out = new ArrayList<>(kapot);
		out.addAll(MachineOverview.lines(entries));
		if (out.isEmpty()) out.add("Geen machines binnen " + RADIUS + " blokken.");
		return out;
	}
}
