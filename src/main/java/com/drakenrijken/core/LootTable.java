package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/** Buittabel van een schatkist: wat erin kan zitten hangt af van het tijdperk van de speler. */
public final class LootTable {
	public record Entry(String item, int count, int weight, int minEra) {
		public Entry {
			if (item == null || item.isBlank()) throw new IllegalArgumentException("leeg item-id");
			if (count < 1) throw new IllegalArgumentException("aantal moet minstens 1 zijn: " + item);
			if (weight < 1) throw new IllegalArgumentException("gewicht moet minstens 1 zijn: " + item);
			if (minEra < 1 || minEra > 3) throw new IllegalArgumentException("tijdperk buiten 1..3: " + item);
		}
	}

	private final List<Entry> entries;

	public LootTable(List<Entry> entries) {
		this.entries = List.copyOf(entries);
	}

	/** Een trekking voor dit tijdperk; leeg als er niets mogelijk is. Nooit een item uit een hoger tijdperk. */
	public Optional<Entry> roll(int era, Random random) {
		if (era < 1 || era > 3) throw new IllegalArgumentException("tijdperk buiten 1..3: " + era);
		List<Entry> possible = new ArrayList<>();
		int total = 0;
		for (Entry e : entries) {
			if (e.minEra() <= era) {
				possible.add(e);
				total += e.weight();
			}
		}
		if (possible.isEmpty()) return Optional.empty();
		int pick = random.nextInt(total);
		for (Entry e : possible) {
			pick -= e.weight();
			if (pick < 0) return Optional.of(e);
		}
		throw new IllegalStateException("onbereikbaar");
	}
}
