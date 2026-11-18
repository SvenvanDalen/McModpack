package com.drakenrijken.core;

/**
 * Grote stad van 64x64 met 25 woningen (24 huizen en de smederij, waar de smid woont). Inwoners zijn een getal
 * (woningen x bewoners per woning), geen echte figuren: zichtbaar blijven er hooguit {@link EntityBudget#MAX_NPCS},
 * zodat de laptop 40+ FPS houdt.
 */
public final class CityPlans {
	public static final int SIZE = 64;
	public static final int INHABITANTS_PER_HOME = 8;

	private CityPlans() {}

	/** Inwoners van een plan: aantal bedden (een per woning) keer bewoners per woning. */
	public static int inhabitants(Blueprint plan) {
		return plan.count("white_bed") * INHABITANTS_PER_HOME;
	}

	/** Heeft dit koninkrijk (zonder burcht, dungeon of drakengebied ernaast) een grote stad? Ongeveer een op de drie; deterministisch. */
	public static boolean isCity(long seed, int chunkX, int chunkZ) {
		if (!KingdomPlacement.companion(seed, chunkX, chunkZ).isEmpty()) return false;
		return new java.util.Random(seed ^ (chunkX * 341873128712L) ^ (chunkZ * 132897987541L)).nextInt(3) == 0;
	}

	public static Blueprint city() {
		Blueprint b = new Blueprint();
		b.fill(0, -1, 0, SIZE - 1, -1, SIZE - 1, "grass_block");
		for (int i = 0; i < 6; i++) {
			b.fill(i * 12 + 1, -1, 0, i * 12 + 2, -1, SIZE - 1, "stone_bricks");
			b.fill(0, -1, i * 12 + 1, SIZE - 1, -1, i * 12 + 2, "stone_bricks");
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i == 2 && j == 2) continue; // midden: smederij met kramen ervoor
				house(b, i * 12 + 4, j * 12 + 4);
			}
		}
		b.room(28, 0, 28, 34, 4, 34, "cobblestone");
		b.fill(31, 1, 28, 31, 2, 28, "air");
		b.set(30, 1, 33, "anvil");
		b.set(32, 1, 33, "furnace");
		b.set(29, 1, 33, "white_bed");
		b.set(26, 0, 27, "hay_block");
		b.set(26, 0, 36, "hay_block");
		b.spot(31, 1, 31, "smid");
		b.spot(26, 0, 31, "marktkoopman");
		for (int k = 0; k < 4; k++) b.spot(4 + k * 12, 1, 3, "dorpeling");
		return b;
	}

	private static void house(Blueprint b, int x0, int z0) {
		b.room(x0, 0, z0, x0 + 6, 4, z0 + 6, "oak_planks");
		b.fill(x0 + 3, 1, z0, x0 + 3, 2, z0, "air");
		b.set(x0 + 1, 1, z0 + 5, "white_bed");
	}
}
