package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.List;

/** Een bouwplan: blokken met een naam op een plek ten opzichte van een hoek. Zonder Minecraft-types. */
public final class Blueprint {
	public record Placement(int x, int y, int z, String block) {}

	private final List<Placement> placements = new ArrayList<>();

	public void set(int x, int y, int z, String block) { placements.add(new Placement(x, y, z, block)); }

	/** Vult een doos (grenzen inclusief) met één blok. */
	public void fill(int x1, int y1, int z1, int x2, int y2, int z2, String block) {
		for (int x = x1; x <= x2; x++) for (int y = y1; y <= y2; y++) for (int z = z1; z <= z2; z++) set(x, y, z, block);
	}

	/** Holle kamer: muren, vloer en dak van wand, binnenin lucht. */
	public void room(int x1, int y1, int z1, int x2, int y2, int z2, String wall) {
		fill(x1, y1, z1, x2, y2, z2, wall);
		fill(x1 + 1, y1 + 1, z1 + 1, x2 - 1, y2 - 1, z2 - 1, "air");
	}

	public List<Placement> placements() { return List.copyOf(placements); }

	public int count(String block) {
		int n = 0;
		for (Placement p : placements) if (p.block().equals(block)) n++;
		return n;
	}
}
