package com.drakenrijken.core;

/** Het land rond het Japanse koninkrijk (DR-1204): kersenbomen, bamboe, een vijver en een kleine berg. Alleen vanilla-blokken. */
public final class JapaneseLand {
	/** Zijde van de tuin in blokken. */
	public static final int SIZE = 32;

	private JapaneseLand() {}

	/** Tuin van 32x32 (circa 1.600 plaatsingen): vijver linksvoor, kersenbomen, bamboebosje en een berg van stenen lagen. */
	public static Blueprint garden() {
		Blueprint b = new Blueprint();
		b.fill(0, -1, 0, SIZE - 1, -1, SIZE - 1, "grass_block");
		// Vijver van 8x8 met een rand van zand.
		b.fill(3, -1, 3, 12, -1, 12, "sand");
		b.fill(4, -1, 4, 11, -1, 11, "water");
		// Kersenbomen: stam van vier blokken en een kruin van 5x5.
		for (int[] t : new int[][] {{3, 18}, {9, 22}, {16, 5}, {14, 14}}) cherry(b, t[0], t[1]);
		// Bamboebosje: twaalf stengels van wisselende hoogte.
		for (int i = 0; i < 12; i++) b.fill(20 + (i * 5) % 9, 0, 3 + (i * 7) % 9, 20 + (i * 5) % 9, 2 + i % 3, 3 + (i * 7) % 9, "bamboo");
		// Kleine berg: vijf afnemende lagen steen met een top van sneeuw.
		for (int l = 0; l < 5; l++) b.fill(19 + l, l, 19 + l, 30 - l, l, 30 - l, "stone");
		b.set(24, 5, 24, "snow_block");
		return b;
	}

	private static void cherry(Blueprint b, int x, int z) {
		b.fill(x, 0, z, x, 3, z, "cherry_log");
		b.fill(x - 2, 4, z - 2, x + 2, 5, z + 2, "cherry_leaves");
		b.fill(x - 1, 6, z - 1, x + 1, 6, z + 1, "cherry_leaves");
		b.set(x, 4, z, "cherry_log");
	}
}
