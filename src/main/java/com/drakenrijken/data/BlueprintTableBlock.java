package com.drakenrijken.data;

import com.drakenrijken.core.Blueprint;
import com.drakenrijken.core.BlueprintCost;
import com.drakenrijken.core.Blueprints;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Bouwplantafel (#147): rechtsklikken zet de werkplaats (waterrad, as, smederij) naast de tafel neer, als je de blokken in je inventaris hebt. */
public class BlueprintTableBlock extends Block {
	public BlueprintTableBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (level instanceof ServerLevel sl) player.sendOverlayMessage(Component.literal(build(player, sl, pos.above(), Blueprints.workshop())));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Zet het plan met de hoek op {@code corner} neer en haalt de materialen uit de inventaris; geeft een korte melding terug. */
	public static String build(Player player, ServerLevel level, BlockPos corner, Blueprint plan) {
		if (!BlueprintCost.fits(plan)) return "Dit plan is te groot (maximaal " + BlueprintCost.MAX_SIZE + " blokken per kant).";
		Map<String, Integer> have = new HashMap<>();
		var inv = player.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack s = inv.getItem(i);
			if (!s.isEmpty()) have.merge(BuiltInRegistries.ITEM.getKey(s.getItem()).toString(), s.getCount(), Integer::sum);
		}
		var missing = BlueprintCost.missing(plan, have);
		if (!missing.isEmpty()) {
			StringBuilder sb = new StringBuilder("Je mist nog:");
			missing.forEach((k, v) -> sb.append(' ').append(v).append("x ").append(k).append(','));
			sb.setLength(sb.length() - 1);
			return sb.toString();
		}
		for (var p : plan.placements()) {
			if (p.block().equals("air")) continue;
			if (!level.getBlockState(corner.offset(p.x(), p.y(), p.z())).canBeReplaced()) return "Er staat iets in de weg; maak de ruimte naast de tafel vrij.";
		}
		for (var e : BlueprintCost.materials(plan).entrySet()) MachineBlock.remove(player, e.getKey(), e.getValue());
		int n = StructureBuilder.place(level, corner, plan);
		return "Gebouwd: " + n + " blokken.";
	}
}
