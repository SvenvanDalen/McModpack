package com.drakenrijken.data;

import com.drakenrijken.core.FastTravel;
import com.drakenrijken.core.TrainLine;
import com.drakenrijken.core.WorldMap;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Treinstation (#144): rechtsklikken brengt je naar het volgende ontdekte koninkrijk, voor de helft van de snelreisprijs,
 * mits een brandende stoomketel met water binnen {@link #BOILER_RADIUS} blokken staat. Geen rails of wagons: dat houdt de laptop snel.
 */
public class TrainStationBlock extends Block {
	public static final int BOILER_RADIUS = 6;

	public TrainStationBlock(Properties properties) { super(properties); }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		if (player instanceof ServerPlayer sp && level instanceof ServerLevel sl) sp.sendOverlayMessage(Component.literal(ride(sl, sp, pos)));
		return InteractionResult.SUCCESS_SERVER;
	}

	/** Kracht van de sterkste stoomketel in de buurt. */
	public static int boilerPower(ServerLevel level, BlockPos pos) {
		int best = 0;
		for (BlockPos p : BlockPos.betweenClosed(pos.offset(-BOILER_RADIUS, -2, -BOILER_RADIUS), pos.offset(BOILER_RADIUS, 2, BOILER_RADIUS))) {
			if (level.getBlockState(p).is(ModBlocks.STOOMKETEL)) best = Math.max(best, SteamBoilerBlock.power(level, p));
		}
		return best;
	}

	/** Bepaalt vertrek (ontdekt koninkrijk bij de speler) en bestemming (het volgende in de lijst) en rijdt. */
	public static String ride(ServerLevel level, ServerPlayer p, BlockPos station) {
		var data = ModData.of(p);
		List<String> ids = new ArrayList<>(data.discovered());
		String from = null;
		for (String id : ids) {
			String[] c = id.split(",");
			long dx = Integer.parseInt(c[0]) - p.getBlockX(), dz = Integer.parseInt(c[1]) - p.getBlockZ();
			if (dx * dx + dz * dz <= (long) WorldMap.DISCOVER_RADIUS * WorldMap.DISCOVER_RADIUS) { from = id; break; }
		}
		if (from == null) return "Het station staat niet bij een ontdekt koninkrijk.";
		if (ids.size() < 2) return "Je kent nog maar één koninkrijk; ontdek er een tweede.";
		String to = ids.get((ids.indexOf(from) + 1) % ids.size());
		var ft = new FastTravel();
		for (String id : ids) {
			String[] c = id.split(",");
			ft.register(new FastTravel.Village(id, Integer.parseInt(c[0]), Integer.parseInt(c[1])));
		}
		var res = TrainLine.check(ft, data.discovered(), from, to, FastTravelService.count(p), boilerPower(level, station));
		int prijs = TrainLine.price(ft, from, to);
		switch (res) {
			case NO_POWER: return "De trein heeft stoom nodig: zet een brandende stoomketel met water binnen " + BOILER_RADIUS + " blokken.";
			case TOO_POOR: return "Te weinig smaragden: een kaartje kost " + prijs + ".";
			case OK: break;
			default: return "De trein kan nu niet rijden.";
		}
		FastTravelService.pay(p, prijs);
		String[] c = to.split(",");
		int x = Integer.parseInt(c[0]), z = Integer.parseInt(c[1]);
		long dx = x - p.getBlockX(), dz = z - p.getBlockZ();
		int seconden = TrainLine.travelSeconds((long) Math.sqrt((double) (dx * dx + dz * dz)));
		level.getChunk(x >> 4, z >> 4);
		p.teleportTo(x + 0.5, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), z + 0.5);
		return "De trein bracht je in " + seconden + " s (uitgespaard) naar " + to + " voor " + prijs + " smaragden.";
	}
}
