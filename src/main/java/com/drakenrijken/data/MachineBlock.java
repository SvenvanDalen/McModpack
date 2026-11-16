package com.drakenrijken.data;

import com.drakenrijken.core.MachineRecipe;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Eenvoudige machine: rechtsklikken verwerkt het eerste passende recept uit de receptenlijst met de spullen
 * uit je inventaris, direct en zonder kracht of wachttijd. Tijdelijk, tot er een echt machinescherm is (DR-410).
 */
public class MachineBlock extends Block {
	private final String machine;

	public MachineBlock(Properties properties, String machine) {
		super(properties);
		this.machine = machine;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		String melding = process(player, machine);
		player.sendOverlayMessage(Component.literal(melding));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Verwerkt één keer het eerste passende recept; geeft een korte melding terug. */
	public static String process(Player player, String machine) {
		Map<String, Integer> have = new HashMap<>();
		var inv = player.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack s = inv.getItem(i);
			if (!s.isEmpty()) have.merge(BuiltInRegistries.ITEM.getKey(s.getItem()).toString(), s.getCount(), Integer::sum);
		}
		var found = ModRecipes.book().find(machine, have);
		if (found.isEmpty()) return "Geen passend recept met wat je bij je hebt.";
		MachineRecipe r = found.get();
		Identifier outId = Identifier.tryParse(r.output());
		var outItem = outId == null ? java.util.Optional.<net.minecraft.world.item.Item>empty() : BuiltInRegistries.ITEM.getOptional(outId);
		if (outItem.isEmpty()) return "Onbekend item in recept " + r.id();
		for (var e : r.inputs().entrySet()) remove(player, e.getKey(), e.getValue());
		ItemStack out = new ItemStack(outItem.get(), r.outputCount());
		player.getInventory().add(out);
		return "Klaar: " + r.outputCount() + "x " + r.output();
	}

	private static void remove(Player player, String itemId, int count) {
		var inv = player.getInventory();
		int left = count;
		for (int i = 0; i < inv.getContainerSize() && left > 0; i++) {
			ItemStack s = inv.getItem(i);
			if (!s.isEmpty() && BuiltInRegistries.ITEM.getKey(s.getItem()).toString().equals(itemId)) {
				int take = Math.min(left, s.getCount());
				s.shrink(take);
				left -= take;
			}
		}
	}
}
