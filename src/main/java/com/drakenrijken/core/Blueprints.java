package com.drakenrijken.core;

/** De bouwplannen van het spel (DR-304 eerste stuk: dungeon). */
public final class Blueprints {
	public static final int DUNGEON_ROOMS = 3;

	private Blueprints() {}

	/**
	 * Dungeon van 3 kamers van 7x5x7 achter elkaar in de x-richting, verbonden door gangen.
	 * Kamer 2 heeft een val (magmablokken in de vloer), kamer 3 de schatkist.
	 * Hoek (0,0,0) is linksonder; de ingang zit aan de westkant.
	 */
	public static Blueprint dungeon() {
		Blueprint b = new Blueprint();
		for (int i = 0; i < DUNGEON_ROOMS; i++) {
			int x0 = i * 9;
			b.room(x0, 0, 0, x0 + 6, 4, 6, "stone_bricks");
			if (i > 0) b.fill(x0 - 1, 1, 3, x0, 2, 3, "air");
		}
		b.fill(0, 1, 3, 0, 2, 3, "air");
		b.fill(10, 0, 2, 12, 0, 4, "magma_block");
		b.set(21, 1, 3, "chest");
		b.set(19, 3, 1, "lantern");
		return b;
	}

	/**
	 * Klein dorp op een vlakke plek van 30x12: drie huizen, een markt met twee kramen en een smederij.
	 * Elk gebouw heeft een deur-opening aan de zuidkant (z=0). NPC-plekken staan in {@link Blueprint#spots()}.
	 */
	public static Blueprint village() {
		Blueprint b = new Blueprint();
		b.fill(0, -1, 0, 29, -1, 11, "grass_block");
		for (int i = 0; i < 3; i++) {
			int x0 = i * 7;
			b.room(x0, 0, 4, x0 + 5, 4, 10, "oak_planks");
			b.fill(x0 + 2, 1, 4, x0 + 3, 2, 4, "air");
			b.set(x0 + 1, 1, 9, "white_bed");
			b.spot(x0 + 3, 1, 7, "dorpeling");
		}
		b.fill(22, 0, 0, 28, 0, 3, "stone_bricks");
		b.set(23, 1, 1, "hay_block");
		b.set(27, 1, 1, "hay_block");
		b.spot(25, 1, 2, "marktkoopman");
		b.room(22, 0, 5, 28, 4, 11, "cobblestone");
		b.fill(24, 1, 5, 25, 2, 5, "air");
		b.set(26, 1, 10, "anvil");
		b.set(24, 1, 10, "furnace");
		b.spot(25, 1, 8, "smid");
		return b;
	}

	/**
	 * Burcht van 21x21: muur met vier torens, een zaal met troon en een kerker eronder met cellen en een kist.
	 * De poort zit aan de zuidkant (z=0). De kerker ligt op y=-5 tot -1 en is bereikbaar via een smalle schacht (x=8, z=9) in de zaalvloer.
	 */
	public static Blueprint castle() {
		Blueprint b = new Blueprint();
		b.room(0, 0, 0, 20, 5, 20, "stone_bricks");
		b.fill(9, 1, 0, 11, 3, 0, "air");
		for (int x : new int[] {0, 17}) for (int z : new int[] {0, 17}) b.room(x, 0, z, x + 3, 9, z + 3, "stone_bricks");
		b.set(10, 1, 15, "gold_block");
		b.spot(10, 1, 13, "koning");
		b.room(4, -5, 4, 16, -1, 16, "cobblestone");
		for (int i = 0; i < 4; i++) b.fill(5 + i * 3, -4, 5, 5 + i * 3, -2, 8, "iron_bars");
		b.set(10, -4, 14, "chest");
		b.spot(10, -4, 11, "bewaker");
		b.fill(8, -4, 9, 8, 0, 9, "air");
		return b;
	}
}
