package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Geruchten in de herberg (#157): kiest een gerucht dat past bij wat de speler al ontdekte en nog niet eerder hoorde. */
public final class RumorTable {
	/** requires: plek die ontdekt moet zijn (leeg = altijd); reveals: plek die het gerucht verraadt (leeg = geen). */
	public record Rumor(String id, String text, String requires, String reveals) {}

	private final List<Rumor> rumors;

	public RumorTable(List<Rumor> rumors) { this.rumors = List.copyOf(rumors); }

	/** Geruchten die nu mogen: voorwaarde vervuld en nog niet gehoord. */
	public List<Rumor> available(Set<String> discovered, Set<String> heard) {
		List<Rumor> r = new ArrayList<>();
		for (Rumor x : rumors) {
			if (heard.contains(x.id())) continue;
			if (!x.requires().isEmpty() && !discovered.contains(x.requires())) continue;
			r.add(x);
		}
		return r;
	}

	/** Kiest met een worp in [0,1); null als er niets (nieuws) is. */
	public Rumor pick(Set<String> discovered, Set<String> heard, double roll) {
		List<Rumor> r = available(discovered, heard);
		if (r.isEmpty()) return null;
		return r.get(Math.min(r.size() - 1, (int) (Math.max(0, roll) * r.size())));
	}
}
