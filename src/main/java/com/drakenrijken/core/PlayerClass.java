package com.drakenrijken.core;

import java.util.Optional;

public enum PlayerClass {
	RIDDER, MAGIER, SCHURK;

	/** Zoekt een klasse op naam, zonder hoofdletters; onbekend geeft leeg. */
	public static Optional<PlayerClass> parse(String name) {
		if (name == null) return Optional.empty();
		for (PlayerClass c : values()) {
			if (c.name().equalsIgnoreCase(name.trim())) return Optional.of(c);
		}
		return Optional.empty();
	}
}
