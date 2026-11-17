package com.drakenrijken.core;

import java.util.HashMap;
import java.util.Map;

/** Kosten en grootte van een bouwplan (#147): telt de blokken, controleert de maximale grootte en of de speler genoeg heeft. Lucht kost niets. */
public final class BlueprintCost {
	public static final int MAX_SIZE = 16;

	private BlueprintCost() {}

	/** Benodigd materiaal per bloknaam; de laatste plaatsing op een plek wint en "air" telt niet mee. */
	public static Map<String, Integer> materials(Blueprint plan) {
		Map<String, String> last = new java.util.LinkedHashMap<>();
		for (Blueprint.Placement p : plan.placements()) last.put(p.x() + "," + p.y() + "," + p.z(), p.block());
		Map<String, Integer> m = new HashMap<>();
		for (String b : last.values()) if (!b.equals("air")) m.merge(b, 1, Integer::sum);
		return m;
	}

	/** Past het plan in een doos van 16x16x16? Een leeg plan past altijd. */
	public static boolean fits(Blueprint plan) {
		if (plan.placements().isEmpty()) return true;
		int[] min = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
		int[] max = {Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
		for (Blueprint.Placement p : plan.placements()) {
			int[] c = {p.x(), p.y(), p.z()};
			for (int i = 0; i < 3; i++) {
				min[i] = Math.min(min[i], c[i]);
				max[i] = Math.max(max[i], c[i]);
			}
		}
		for (int i = 0; i < 3; i++) if (max[i] - min[i] + 1 > MAX_SIZE) return false;
		return true;
	}

	/** Heeft de speler van alles genoeg? */
	public static boolean affordable(Blueprint plan, Map<String, Integer> have) {
		return missing(plan, have).isEmpty();
	}

	/** Wat er nog ontbreekt, per bloknaam; leeg als alles er is. */
	public static Map<String, Integer> missing(Blueprint plan, Map<String, Integer> have) {
		Map<String, Integer> r = new HashMap<>();
		for (var e : materials(plan).entrySet()) {
			int tekort = e.getValue() - Math.max(0, have.getOrDefault(e.getKey(), 0));
			if (tekort > 0) r.put(e.getKey(), tekort);
		}
		return r;
	}
}
