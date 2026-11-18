package com.drakenrijken.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Belastingkist (#153): rechtsklikken haalt de belasting van je rijk op, hoogstens {@link com.drakenrijken.core.RealmTax#MAX_DAYS} dagen terug. */
public class TaxChestBlock extends Block {
	public TaxChestBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player instanceof ServerPlayer sp) sp.sendOverlayMessage(Component.literal(collect(sp)));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Betaalt de openstaande belasting uit in goudstaven en geeft een korte melding terug. */
	public static String collect(ServerPlayer p) {
		PlayerDataHolder h = new PlayerDataHolder(p);
		int niveau = com.drakenrijken.core.RealmState.levelFor(h.data.realmPower());
		long dag = p.level().getGameTime() / 24000L;
		int belasting = com.drakenrijken.core.RealmTax.owed(niveau, h.data.lastTaxDay(), dag);
		int opbrengst = com.drakenrijken.core.Profession.payout(niveau, h.data.lastTaxDay(), dag);
		int bedrag = belasting + opbrengst;
		if (bedrag <= 0) return niveau == 0 ? "Je rijk is nog te klein om belasting te betalen (niveau 1 nodig)." : "Er ligt nog geen belasting klaar; kom morgen terug.";
		h.data.setLastTaxDay(dag);
		p.setAttached(ModData.PLAYER_DATA, h.data);
		p.getInventory().add(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GOLD_INGOT, bedrag));
		return "Je rijk betaalt " + belasting + " goudstaven belasting en je dorpelingen leveren " + opbrengst + " op.";
	}

	private record PlayerDataHolder(com.drakenrijken.core.PlayerData data) {
		PlayerDataHolder(ServerPlayer p) { this(ModData.of(p)); }
	}
}
