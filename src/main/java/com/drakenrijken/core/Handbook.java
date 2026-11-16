package com.drakenrijken.core;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** De handleiding in het spel (DR-706): hoofdstukken met korte regels uitleg. */
public record Handbook(List<Chapter> chapters) {
	public record Chapter(String id, String title, List<String> lines) {
		public Chapter {
			if (id == null || id.isBlank() || title == null || title.isBlank()) throw new IllegalArgumentException("hoofdstuk mist id of titel");
			lines = List.copyOf(lines);
		}
	}

	public Handbook {
		chapters = List.copyOf(chapters);
	}

	public Optional<Chapter> find(String id) {
		String key = id.toLowerCase(Locale.ROOT);
		return chapters.stream().filter(c -> c.id().equals(key)).findFirst();
	}

	/** Overzicht van alle hoofdstukken, om te kiezen. */
	public String index() {
		if (chapters.isEmpty()) return "De handleiding is leeg.";
		StringBuilder sb = new StringBuilder("Handleiding - kies met /dr handleiding <onderwerp>:");
		for (Chapter c : chapters) sb.append("\n- ").append(c.id()).append(": ").append(c.title());
		return sb.toString();
	}

	public String describe(Chapter c) {
		return c.title() + "\n" + String.join("\n", c.lines());
	}
}
