package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/** Spawnlijst van een gebied: welke vijanden er kunnen verschijnen, afhankelijk van het tijdperk. */
public final class SpawnTable {
	public record Entry(String mob, int weight, int minEra) {
		public Entry {
			if (mob == null || mob.isBlank()) throw new IllegalArgumentException("leeg vijand-id");
			if (weight < 1) throw new IllegalArgumentException("gewicht moet minstens 1 zijn: " + mob);
			if (minEra < 1 || minEra > 3) throw new IllegalArgumentException("tijdperk buiten 1..3: " + mob);
		}
	}

	private final String area;
	private final List<Entry> entries;

	public SpawnTable(String area, List<Entry> entries) {
		if (area == null || area.isBlank()) throw new IllegalArgumentException("leeg gebied-id");
		this.area = area;
		this.entries = List.copyOf(entries);
	}

	public String area() { return area; }

	/** Kiest een vijand voor dit tijdperk; leeg als er niets mogelijk is. Nooit een vijand uit een hoger tijdperk. */
	public Optional<Entry> pick(int era, Random random) {
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
