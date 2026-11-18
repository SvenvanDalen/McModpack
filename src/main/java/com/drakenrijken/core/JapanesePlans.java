package com.drakenrijken.core;

/** De bouwplannen van het Japanse koninkrijk (DR-1203): dorp met torii, dojo en pagode, en een kasteel. Alleen vanilla-blokken. */
public final class JapanesePlans {
	/** Zijde van het dorpsplan in blokken (past in de vlakte-controle van 32x32 in WorldGen). */
	public static final int VILLAGE_SIZE = 32;

	private JapanesePlans() {}

	/**
	 * Dorp van 32x32: torii-poort aan de zuidkant (z=0), een pad naar het midden, zes huizen, een markt, een dojo,
	 * een smederij en een pagode van drie lagen. Elk gebouw heeft zijn deur aan de zuidkant. NPC-plekken staan in de lucht.
	 */
	public static Blueprint village() {
		Blueprint b = new Blueprint();
		b.fill(0, -1, 0, 31, -1, 31, "grass_block");
		b.fill(15, -1, 0, 16, -1, 22, "dirt_path");
		// Torii: twee rode pilaren, een rode dwarsbalk en een donkere dakbalk.
		for (int x : new int[] {12, 19}) b.fill(x, 0, 0, x, 4, 0, "red_concrete");
		b.fill(13, 3, 0, 18, 3, 0, "red_concrete");
		b.fill(11, 5, 0, 20, 5, 0, "dark_oak_planks");
		// Huizen: vier in de eerste rij, één in de tweede en één in de derde.
		house(b, 2, 4);
		house(b, 9, 4);
		house(b, 19, 4);
		house(b, 25, 4);
		house(b, 2, 14);
		house(b, 2, 24);
		// Markt met twee kramen.
		b.fill(8, 0, 13, 13, 0, 20, "stone_bricks");
		for (int x : new int[] {9, 12}) for (int z : new int[] {15, 17}) b.fill(x, 1, z, x, 2, z, "spruce_fence");
		b.fill(9, 3, 15, 12, 3, 17, "red_wool");
		b.set(10, 1, 16, "hay_block");
		b.set(11, 1, 16, "hay_block");
		b.spot(11, 1, 19, "marktkoopman");
		// Dojo.
		b.room(18, 0, 13, 26, 4, 20, "dark_oak_planks");
		b.fill(21, 1, 13, 23, 2, 13, "air");
		b.set(18, 2, 16, "white_stained_glass_pane");
		b.set(26, 2, 16, "white_stained_glass_pane");
		b.set(19, 3, 14, "lantern");
		b.set(25, 3, 14, "lantern");
		b.fill(17, 5, 12, 27, 5, 21, "cherry_planks");
		b.spot(22, 1, 17, "sensei");
		// Smederij.
		b.room(9, 0, 24, 14, 3, 29, "cobblestone");
		b.fill(11, 1, 24, 12, 2, 24, "air");
		b.set(12, 1, 28, "anvil");
		b.set(10, 1, 28, "furnace");
		b.fill(8, 4, 23, 15, 4, 30, "cobblestone_slab");
		b.spot(11, 1, 26, "smid");
		// Pagode: drie lagen met steeds een dak van donkere planken.
		b.room(21, 0, 24, 27, 3, 30, "spruce_planks");
		b.fill(24, 1, 24, 24, 2, 24, "air");
		b.set(22, 3, 25, "lantern");
		b.fill(20, 4, 23, 28, 4, 31, "dark_oak_slab");
		b.room(22, 5, 25, 26, 7, 29, "spruce_planks");
		b.fill(21, 8, 24, 27, 8, 30, "dark_oak_slab");
		b.room(23, 9, 26, 25, 11, 28, "spruce_planks");
		b.fill(22, 12, 25, 26, 12, 29, "dark_oak_slab");
		b.fill(24, 13, 27, 24, 16, 27, "dark_oak_fence");
		return b;
	}

	/** Huis van 5x6 met deur aan de zuidkant, bed, ramen, lantaarn en een dak met overhang; hoek (x0, z0). */
	private static void house(Blueprint b, int x0, int z0) {
		b.room(x0, 0, z0, x0 + 4, 3, z0 + 5, "spruce_planks");
		b.fill(x0 + 2, 1, z0, x0 + 2, 2, z0, "air");
		b.set(x0, 2, z0 + 3, "white_stained_glass_pane");
		b.set(x0 + 4, 2, z0 + 3, "white_stained_glass_pane");
		b.set(x0 + 1, 1, z0 + 4, "white_bed");
		b.set(x0 + 3, 2, z0 + 4, "lantern");
		b.fill(x0 - 1, 4, z0 - 1, x0 + 5, 4, z0 + 6, "dark_oak_slab");
		b.spot(x0 + 3, 1, z0 + 2, "dorpeling");
	}

	/**
	 * Kasteel van 21x21: een stenen voet met een troonzaal en een poort met torii aan de zuidkant (z=0),
	 * vier hoektorens, een witte keep met drie daken erboven en onder de zaal dezelfde kerker als de burcht
	 * (y=-5 tot -1, bereikbaar via een schacht op x=8, z=9).
	 */
	public static Blueprint castle() {
		Blueprint b = new Blueprint();
		b.room(0, 0, 0, 20, 4, 20, "stone_bricks");
		b.fill(9, 1, 0, 11, 3, 0, "air");
		for (int x : new int[] {8, 12}) b.fill(x, 1, 0, x, 3, 0, "red_concrete");
		b.fill(8, 4, 0, 12, 4, 0, "red_concrete");
		for (int x : new int[] {0, 17}) for (int z : new int[] {0, 17}) {
			b.room(x, 0, z, x + 3, 7, z + 3, "stone_bricks");
			b.fill(x, 8, z, x + 3, 8, z + 3, "dark_oak_slab");
		}
		b.set(10, 1, 15, "gold_block");
		b.spot(10, 1, 13, "koning");
		b.set(6, 3, 6, "lantern");
		b.set(14, 3, 6, "lantern");
		// Keep boven de troonzaal.
		b.room(6, 4, 6, 14, 9, 14, "white_concrete");
		b.fill(5, 10, 5, 15, 10, 15, "dark_oak_slab");
		b.room(8, 11, 8, 12, 14, 12, "white_concrete");
		b.fill(7, 15, 7, 13, 15, 13, "dark_oak_slab");
		b.fill(10, 16, 10, 10, 19, 10, "dark_oak_fence");
		// Kerker, gelijk aan die van de burcht.
		b.room(4, -5, 4, 16, -1, 16, "cobblestone");
		for (int i = 0; i < 4; i++) b.fill(5 + i * 3, -4, 5, 5 + i * 3, -2, 8, "iron_bars");
		b.set(10, -4, 14, "chest");
		b.spot(10, -4, 11, "bewaker");
		b.fill(8, -4, 9, 8, 0, 9, "air");
		return b;
	}
}
