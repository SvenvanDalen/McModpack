package com.drakenrijken.data;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/** Moersleutel (#149): rechtsklik op een machine zet slijtage terug naar nul en verhelpt een storing. */
public class WrenchItem extends Item {
	public WrenchItem(Properties properties) { super(properties); }

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (!(context.getLevel() instanceof ServerLevel level)) return InteractionResult.SUCCESS;
		if (!(level.getBlockState(context.getClickedPos()).getBlock() instanceof MachineBlock)) return InteractionResult.PASS;
		String melding = MachineRuntime.repair(level, context.getClickedPos()) ? "Machine hersteld." : "Deze machine mankeert niets.";
		if (context.getPlayer() != null) context.getPlayer().sendOverlayMessage(Component.literal(melding));
		return InteractionResult.SUCCESS_SERVER;
	}
}
