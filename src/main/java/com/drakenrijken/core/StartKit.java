package com.drakenrijken.core;

import java.util.List;
import java.util.Map;

/** De startset van een klasse: item-id's met aantallen. */
public record StartKit(PlayerClass playerClass, Map<String, Integer> items) {
	public StartKit {
		if (playerClass == null) throw new IllegalArgumentException("klasse ontbreekt");
		for (var e : items.entrySet()) {
			if (e.getKey() == null || e.getKey().isBlank()) throw new IllegalArgumentException("leeg item-id");
			if (e.getValue() < 1) throw new IllegalArgumentException("aantal moet minstens 1 zijn: " + e.getKey());
		}
		items = Map.copyOf(items);
	}

	/** De kit van een klasse uit een lijst; leeg als de klasse er niet in staat. */
	public static StartKit forClass(List<StartKit> kits, PlayerClass c) {
		return kits.stream().filter(k -> k.playerClass() == c).findFirst().orElse(new StartKit(c, Map.of()));
	}
}
