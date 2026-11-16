package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Kaart en kompas: een koninkrijk staat pas op de kaart als de speler dichtbij is geweest. */
public final class WorldMap {
	public static final int DISCOVER_RADIUS = 64;

	public enum Direction { N, NE, E, SE, S, SW, W, NW }

	public record Realm(String id, int x, int z) {
		public Realm {
			if (id == null || id.isBlank()) throw new IllegalArgumentException("leeg koninkrijk-id");
		}
	}

	private final Map<String, Realm> realms = new LinkedHashMap<>();
	private final Set<String> discovered = new LinkedHashSet<>();

	public void register(Realm r) {
		if (realms.putIfAbsent(r.id(), r) != null) throw new IllegalArgumentException("dubbel koninkrijk: " + r.id());
	}

	/** Ontdekt alle koninkrijken binnen de straal rond (x, z); geeft alleen de nieuwe terug. */
	public List<String> visit(int x, int z) {
		List<String> nieuw = new ArrayList<>();
		double r2 = (double) DISCOVER_RADIUS * DISCOVER_RADIUS;
		for (Realm r : realms.values()) {
			double dx = (double) r.x() - x;
			double dz = (double) r.z() - z;
			if (dx * dx + dz * dz <= r2 && discovered.add(r.id())) nieuw.add(r.id());
		}
		return nieuw;
	}

	/** De kaartpunten: alleen ontdekte koninkrijken, in volgorde van ontdekking. */
	public List<Realm> markers() {
		List<Realm> out = new ArrayList<>();
		for (String id : discovered) out.add(realms.get(id));
		return out;
	}

	public Set<String> discovered() { return Set.copyOf(discovered); }

	/** Kompasrichting van (x, z) naar een ontdekt koninkrijk. Noord is negatieve z. Staat de speler precies op het koninkrijk, dan is het N. */
	public Direction compass(int x, int z, String realmId) {
		Realm r = realms.get(realmId);
		if (r == null) throw new IllegalArgumentException("onbekend koninkrijk: " + realmId);
		if (!discovered.contains(realmId)) throw new IllegalArgumentException("nog niet ontdekt: " + realmId);
		double dx = (double) r.x() - x;
		double dz = (double) r.z() - z;
		if (dx == 0 && dz == 0) return Direction.N;
		double hoek = Math.toDegrees(Math.atan2(dx, -dz));
		if (hoek < 0) hoek += 360;
		return Direction.values()[(int) Math.floor((hoek + 22.5) / 45) % 8];
	}
}
