package com.drakenrijken.data;

import com.drakenrijken.core.CartRoute;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Karstation (#146): een onzichtbare mijnkar rijdt zijn route; de opbrengst wordt pas bij een klik uitgerekend uit de speltijd,
 * dus het kost geen ticks. Hoogstens {@link #MAX_SECONDS} seconden worden bijgeschreven. De starttijd staat alleen in het geheugen:
 * na een herstart begint de teller opnieuw.
 */
public class CartStationBlock extends Block {
	public static final CartRoute ROUTE = new CartRoute(4, 20);
	public static final int MAX_SECONDS = 1800;
	private static final Map<String, Long> START = new HashMap<>();

	public CartStationBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player instanceof ServerPlayer sp && level instanceof ServerLevel sl) sp.sendOverlayMessage(Component.literal(collect(sp, sl, pos, sl.getGameTime())));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Haalt de kolen op die de kar sinds de vorige keer bracht; de eerste klik zet de kar op weg. */
	public static String collect(ServerPlayer p, ServerLevel level, BlockPos pos, long now) {
		String k = level.dimension().identifier() + "|" + pos.asLong();
		Long start = START.get(k);
		if (start == null) {
			START.put(k, now);
			return "De mijnkar rijdt weg. Kom later terug voor kolen.";
		}
		long seconds = Math.min(MAX_SECONDS, Math.max(0, now - start) / 20);
		long ronden = ROUTE.deliveredIn(seconds);
		if (ronden <= 0) return "De kar is nog onderweg.";
		long volRonden = ROUTE.deliveredIn(seconds) / ROUTE.loadSize();
		START.put(k, start + volRonden * ROUTE.roundSeconds() * 20L);
		p.getInventory().add(new ItemStack(Items.COAL, (int) ronden));
		return "De kar bracht " + ronden + " kolen.";
	}

	/** Alleen voor tests. */
	public static void clearForTest() { START.clear(); }
}
