package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.List;

/** De regels van de HUD (DR-701): quest, balk van de klasse en rijk-niveau. Lege onderdelen vallen weg. */
public final class HudLines {
	private HudLines() {}

	public static String build(String quest, PlayerClass klasse, ResourceBar balk, int rijkNiveau) {
		List<String> r = new ArrayList<>();
		if (!quest.isEmpty()) r.add("Quest: " + quest);
		if (klasse != null && balk != null) {
			r.add((klasse == PlayerClass.MAGIER ? "Mana " : "Uithouding ") + balk.value() + "/" + balk.max());
		}
		if (rijkNiveau > 0) r.add("Rijk niveau " + rijkNiveau);
		return String.join("\n", r);
	}
}
