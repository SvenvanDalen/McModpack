package com.drakenrijken.core;

import java.util.List;

/** Waarden van de Japanse wapens (DR-1205); voorlopige getallen, later te balanceren. */
public final class JapaneseWeapons {
	/** Een shuriken-stapel is maximaal 16. */
	public static final int SHURIKEN_STACK = 16;

	/** Schade (harten x2), bereik in blokken en duurzaamheid (1 voor weggooivoorwerpen). */
	public record Stats(String id, double damage, double reach, int durability) {}

	private static final List<Stats> ALL = List.of(
		new Stats("katana", 7, 2.5, 500),
		new Stats("naginata", 8, 3, 450),
		new Stats("yumi", 6, 30, 384),
		new Stats("shuriken", 3, 12, 1));

	private JapaneseWeapons() {}

	public static List<Stats> all() {
		return ALL;
	}

	public static Stats get(String id) {
		return ALL.stream().filter(s -> s.id().equals(id)).findFirst()
			.orElseThrow(() -> new IllegalArgumentException("onbekend wapen: " + id));
	}

	/** Katana en naginata zijn voor samoerai; yumi en shuriken voor iedereen. */
	public static boolean usableBy(String id, PlayerClass c) {
		get(id);
		return switch (id) {
			case "katana", "naginata" -> c == PlayerClass.SAMOERAI;
			default -> true;
		};
	}
}
