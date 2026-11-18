package com.drakenrijken.core;

/**
 * De vier ruïnes (#158) als bouwplan, volgens docs/ruine-indelingen.md. Hoek (0,0,0) is linksboven (noordwest), y=0 is de eerste laag boven de grond,
 * y=-1 de grond zelf. Raster: kolom = x, rij = z, elk raster 12x12. Elk plan heeft precies één ruinesteen en één kist.
 * Aanpassing op het ontwerp: het uitgedoofde kampvuur is een kolenblok (een kampvuur zou licht en deeltjes kosten).
 */
public final class RuinPlans {
	public static final String STONE = "drakenrijken:ruinesteen";
	public static final String[] STORIES = {"verlaten_wachttoren", "gevallen_smederij", "verzonken_kapel", "drakenkamp"};

	private RuinPlans() {}

	public static Blueprint plan(String story) {
		return switch (story) {
			case "verlaten_wachttoren" -> tower();
			case "gevallen_smederij" -> smithy();
			case "verzonken_kapel" -> chapel();
			case "drakenkamp" -> camp();
			default -> throw new IllegalArgumentException("onbekende ruïne: " + story);
		};
	}

	/** Plek (x, z) van de ruinesteen in het plan. */
	public static int[] stoneAt(Blueprint plan) {
		for (var p : plan.placements()) if (p.block().equals(STONE)) return new int[] {p.x(), p.z()};
		throw new IllegalStateException("plan zonder ruinesteen");
	}

	private static void check(String[] g) {
		if (g.length != 12) throw new IllegalArgumentException("raster moet 12 rijen hebben");
		for (String r : g) if (r.length() != 12) throw new IllegalArgumentException("rij moet 12 tekens hebben: " + r);
	}

	/** Cijfers in het raster zijn muurhoogtes; onderin en op elke derde plek mos. */
	private static void walls(Blueprint b, String[] g, String plain, String mossy) {
		check(g);
		for (int z = 0; z < 12; z++) for (int x = 0; x < 12; x++) {
			char c = g[z].charAt(x);
			if (c < '1' || c > '9') continue;
			for (int y = 0; y < c - '0'; y++) b.set(x, y, z, (y <= 1 || (x + z) % 3 == 0) ? mossy : plain);
		}
	}

	private static void each(String[] g, char ch, java.util.function.BiConsumer<Integer, Integer> f) {
		check(g);
		for (int z = 0; z < 12; z++) for (int x = 0; x < 12; x++) if (g[z].charAt(x) == ch) f.accept(x, z);
	}

	private static Blueprint tower() {
		Blueprint b = new Blueprint();
		String[] w = {
			"............",
			"............",
			"..75656544..",
			"..6.......4.",
			"..5.......3.",
			"..5.......3.",
			"..5.......3.",
			"..4.......3.",
			"..3333.3332.",
			"............",
			"............",
			"............"};
		String[] f = {
			"............",
			"............",
			"............",
			"...S........",
			"............",
			".....BB.....",
			".....PP.....",
			"............",
			"............",
			"...r.....r..",
			"............",
			"............"};
		b.fill(2, -1, 2, 9, -1, 8, "stone");
		walls(b, w, "cobblestone", "mossy_cobblestone");
		each(f, 'B', (x, z) -> b.set(x, -1, z, "blackstone"));
		each(f, 'P', (x, z) -> b.set(x, 0, z, "oak_planks"));
		each(f, 'r', (x, z) -> b.set(x, 0, z, "cobblestone"));
		b.set(8, 0, 6, "iron_bars");
		b.fill(5, -3, 5, 6, -2, 5, "air");
		b.set(5, -1, 5, "air");
		b.set(5, 0, 5, "mossy_cobblestone");
		b.set(6, -3, 5, "chest");
		b.set(3, 0, 3, STONE);
		return b;
	}

	private static Blueprint smithy() {
		Blueprint b = new Blueprint();
		String[] w = {
			"............",
			".5444444445.",
			".4........2.",
			".4........0.",
			".4........0.",
			".4........3.",
			".4........3.",
			".3333..3333.",
			"............",
			"............",
			"............",
			"............"};
		String[] f = {
			"............",
			"............",
			"............",
			"...AA..FF...",
			"........S...",
			"...ZT.......",
			"............",
			"............",
			"............",
			"...BBBB.....",
			"....BB......",
			"............"};
		b.fill(2, -1, 2, 9, -1, 6, "stone");
		walls(b, w, "cobblestone", "cobblestone");
		each(f, 'B', (x, z) -> b.set(x, -1, z, "blackstone"));
		b.fill(6, -1, 2, 9, -1, 4, "blackstone");
		b.set(3, 0, 3, "chipped_anvil");
		b.set(4, 0, 3, "damaged_anvil");
		each(f, 'F', (x, z) -> b.set(x, 0, z, "furnace"));
		b.fill(7, 1, 3, 7, 3, 3, "cobblestone");
		b.set(3, 0, 5, "smoker");
		b.fill(3, -3, 5, 5, -2, 7, "air");
		b.set(4, -1, 5, "oak_trapdoor");
		b.set(5, -3, 7, "chest");
		b.set(8, 0, 4, STONE);
		return b;
	}

	private static Blueprint chapel() {
		Blueprint b = new Blueprint();
		String[] w = {
			"..6666666.6.",
			"..4.......4.",
			"..4.......4.",
			"..4.......4.",
			"..4.......4.",
			"..4.......4.",
			"..4.......3.",
			"..4.......3.",
			"..4.......3.",
			"..3.......3.",
			"..3333.3333."};
		String[] w2 = new String[12];
		System.arraycopy(w, 0, w2, 0, 11);
		w2[11] = "............";
		String[] f = {
			"............",
			".....LO.....",
			"........r...",
			"...U........",
			"............",
			"...SQ..Q....",
			"............",
			"....Q..Q....",
			"............",
			"............",
			"............",
			"............"};
		String[] water = {
			"............",
			"............",
			"............",
			"......WWW...",
			"...WWWWWW...",
			"...W.WW.W...",
			"...WWWWWW...",
			"...WQWWQW...",
			"...WWWWWW...",
			"....WWWW....",
			"............",
			"............"};
		b.fill(2, -1, 0, 9, -1, 10, "stone_bricks");
		walls(b, w2, "cobblestone", "mossy_cobblestone");
		b.set(5, 0, 10, "stone_bricks");
		each(water, 'W', (x, z) -> b.set(x, 0, z, "water"));
		each(f, 'Q', (x, z) -> b.fill(x, 0, z, x, 3, z, "mossy_stone_bricks"));
		b.set(5, 0, 1, "stone_bricks");
		b.set(6, 0, 1, "stone_brick_slab");
		b.set(3, 0, 3, "oak_stairs");
		each(f, 'r', (x, z) -> b.fill(x, 0, z, x, 1, z, "cobblestone"));
		b.fill(8, 4, 1, 9, 7, 2, "cobblestone");
		b.set(5, 0, 0, "chest");
		b.set(3, 0, 5, STONE);
		return b;
	}

	private static Blueprint camp() {
		Blueprint b = new Blueprint();
		String[] rock = {
			"........366.",
			"........3663",
			"........342.",
			"............",
			"............",
			"............",
			"............",
			"............",
			"............",
			"............",
			"............",
			"............"};
		String[] f = {
			"............",
			".TTT........",
			".TTT........",
			".TT.........",
			"..B.....BB..",
			"..BB.FF.B...",
			"...BSFF.....",
			".TTT...s.TTT",
			".TTT.....TTT",
			".TT....B.TT.",
			"......s.....",
			"............"};
		walls(b, rock, "andesite", "stone");
		b.fill(8, 1, 3, 10, 2, 3, "stone");
		each(f, 'B', (x, z) -> b.set(x, -1, z, "blackstone"));
		each(f, 'T', (x, z) -> {
			b.set(x, 0, z, "black_wool");
			if (z >= 7 || x >= 8 || (x + z) % 3 != 0) b.set(x, 1, z, "black_wool");
		});
		each(f, 'F', (x, z) -> b.set(x, 0, z, "coal_block"));
		each(f, 's', (x, z) -> b.fill(x, 0, z, x, 1, z, "oak_fence"));
		b.set(5, 0, 6, "cobblestone");
		b.set(8, 0, 3, "cobblestone");
		b.set(9, 0, 3, "chest");
		b.set(4, 0, 6, STONE);
		return b;
	}
}
