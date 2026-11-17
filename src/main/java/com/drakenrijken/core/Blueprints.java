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
}
