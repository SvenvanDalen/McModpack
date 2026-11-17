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
}
