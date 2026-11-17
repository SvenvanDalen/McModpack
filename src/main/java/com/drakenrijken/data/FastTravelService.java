package com.drakenrijken.data;

import com.drakenrijken.core.FastTravel;
import com.drakenrijken.core.WorldMap;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;

/** Snelreis (DR-310): staat de speler bij een ontdekt koninkrijk, dan kan hij tegen smaragden naar een ander ontdekt koninkrijk. */
public final class FastTravelService {
	private FastTravelService() {}

	/** Reist naar ontdekt koninkrijk nummer `nummer` (1 = eerste in /dr kaart). Geeft een melding voor de speler. */
	public static String travel(ServerLevel level, ServerPlayer p, int nummer) {
		var data = ModData.of(p);
		List<String> ids = new ArrayList<>(data.discovered());
		if (nummer < 1 || nummer > ids.size()) return "Onbekend nummer. Zie /dr kaart.";
		String to = ids.get(nummer - 1);
		String from = null;
		for (String id : ids) {
			String[] c = id.split(",");
			long dx = Integer.parseInt(c[0]) - p.getBlockX(), dz = Integer.parseInt(c[1]) - p.getBlockZ();
			if (dx * dx + dz * dz <= (long) WorldMap.DISCOVER_RADIUS * WorldMap.DISCOVER_RADIUS) { from = id; break; }
		}
		if (from == null) return "Je moet bij een ontdekt koninkrijk staan om te reizen.";
		var ft = new FastTravel();
		for (String id : ids) {
			String[] c = id.split(",");
			ft.register(new FastTravel.Village(id, Integer.parseInt(c[0]), Integer.parseInt(c[1])));
		}
		int coins = count(p);
		var res = ft.check(data.discovered(), from, to, coins);
		switch (res) {
			case SAME_PLACE: return "Je staat al bij dat koninkrijk.";
			case TOO_POOR: return "Te weinig smaragden: de reis kost " + ft.cost(from, to) + ".";
			case OK: break;
			default: return "Reizen kan nu niet.";
		}
		int prijs = ft.cost(from, to);
		pay(p, prijs);
		String[] c = to.split(",");
		int x = Integer.parseInt(c[0]), z = Integer.parseInt(c[1]);
		level.getChunk(x >> 4, z >> 4);
		int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
		p.teleportTo(x + 0.5, y, z + 0.5);
		return "Reis voltooid voor " + prijs + " smaragden.";
	}

	private static int count(ServerPlayer p) {
		int n = 0;
		var inv = p.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			var s = inv.getItem(i);
			if (s.is(Items.EMERALD)) n += s.getCount();
		}
		return n;
	}

	private static void pay(ServerPlayer p, int prijs) {
		var inv = p.getInventory();
		for (int i = 0; i < inv.getContainerSize() && prijs > 0; i++) {
			var s = inv.getItem(i);
			if (s.is(Items.EMERALD)) {
				int n = Math.min(prijs, s.getCount());
				s.shrink(n);
				prijs -= n;
			}
		}
	}
}
