package com.drakenrijken.data;

import com.drakenrijken.core.RuinStory;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Ruïnesteen (#158): elke rechtsklik onthult de volgende aanwijzing (data/drakenrijken/ruins/*.json); na de laatste volgt een beloning, één keer per speler. */
public class RuinStoneBlock extends Block {
	public RuinStoneBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player instanceof ServerPlayer sp) sp.sendSystemMessage(Component.literal(read(sp, level.dimension().identifier() + "|" + pos.asLong())));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Leest de volgende aanwijzing van de ruïne op deze plek; voortgang staat bij de speler. */
	public static String read(ServerPlayer p, String where) {
		var data = ModData.of(p);
		String prefix = "ruine:" + where + ":";
		int gelezen = (int) data.heard().stream().filter(s -> s.startsWith(prefix)).count();
		List<String> clues = new ArrayList<>();
		int reward = 0;
		try (var in = RuinStoneBlock.class.getResourceAsStream("/data/drakenrijken/ruins/verlaten_wachttoren.json")) {
			var o = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
			o.getAsJsonArray("clues").forEach(e -> clues.add(e.getAsString()));
			reward = o.get("reward").getAsInt();
		} catch (java.io.IOException | RuntimeException ex) {
			return "De ruïne zwijgt (verhaal niet geladen).";
		}
		RuinStory story = new RuinStory(clues, "ruine_schat");
		for (int i = 0; i < gelezen; i++) story.nextClue();
		if (story.complete()) return "Je hebt deze ruïne al ontcijferd.";
		String clue = story.nextClue();
		data.heard().add(prefix + gelezen);
		String extra = "";
		if (story.complete()) {
			p.getInventory().add(new ItemStack(Items.GOLD_INGOT, reward));
			extra = " Je vindt " + reward + " goudstaven!";
		}
		p.setAttached(ModData.PLAYER_DATA, data);
		return "Aanwijzing " + story.found() + "/" + clues.size() + ": " + clue + extra;
	}
}
