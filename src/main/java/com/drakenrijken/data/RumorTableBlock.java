package com.drakenrijken.data;

import com.drakenrijken.core.RumorTable;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Geruchtentafel (#157): rechtsklikken laat je een nieuw gerucht horen uit data/drakenrijken/rumors/geruchten.json. */
public class RumorTableBlock extends Block {
	private static RumorTable table;

	public RumorTableBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player instanceof ServerPlayer sp) sp.sendSystemMessage(Component.literal(listen(sp, level.getRandom().nextDouble())));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Kiest een gerucht met de worp, onthoudt dat de speler het hoorde en geeft de tekst terug. */
	public static String listen(ServerPlayer p, double roll) {
		var data = ModData.of(p);
		Set<String> kennis = new HashSet<>(data.discovered());
		if (!data.discovered().isEmpty()) kennis.add("koninkrijk");
		var r = rumors().pick(kennis, data.heard(), roll);
		if (r == null) return "Er is geen nieuws; kom later terug.";
		data.heard().add(r.id());
		p.setAttached(ModData.PLAYER_DATA, data);
		return "Gerucht: " + r.text();
	}

	private static RumorTable rumors() {
		if (table == null) table = load();
		return table;
	}

	private static RumorTable load() {
		List<RumorTable.Rumor> list = new ArrayList<>();
		try (var in = RumorTableBlock.class.getResourceAsStream("/data/drakenrijken/rumors/geruchten.json")) {
			if (in != null) {
				for (var e : JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonArray("rumors")) {
					var o = e.getAsJsonObject();
					list.add(new RumorTable.Rumor(o.get("id").getAsString(), o.get("text").getAsString(), o.get("requires").getAsString(), o.get("reveals").getAsString()));
				}
			}
		} catch (java.io.IOException | RuntimeException ex) {
			System.err.println("Geruchten niet geladen: " + ex.getMessage());
		}
		return new RumorTable(list);
	}
}
