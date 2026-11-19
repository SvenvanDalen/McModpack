package com.drakenrijken.core;

import java.util.List;

/** Waarden van het Japanse eten (DR-1206); voorlopige getallen, later te balanceren. */
public final class JapaneseFood {
	/** Honger (halve drumsticks), verzadiging (vermenigvuldiger) en de naam van de bonus. */
	public record Stats(String id, int hunger, double saturation, String bonus) {}

	private static final List<Stats> ALL = List.of(
		new Stats("rijst", 2, 0.4, "stevig_ontbijt"),
		new Stats("onigiri", 5, 0.6, "uithouding_herstel"),
		new Stats("sushi", 6, 0.8, "snelheid"),
		new Stats("thee", 2, 0.3, "mana_herstel"));

	private JapaneseFood() {}

	public static List<Stats> all() {
		return ALL;
	}

	public static Stats get(String id) {
		return ALL.stream().filter(s -> s.id().equals(id)).findFirst()
			.orElseThrow(() -> new IllegalArgumentException("onbekend gerecht: " + id));
	}
}
