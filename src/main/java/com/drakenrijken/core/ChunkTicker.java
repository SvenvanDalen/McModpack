package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Machines tikken alleen als hun chunk geladen is; zo kost een machine buiten beeld niets. */
public final class ChunkTicker {
	public record Chunk(int x, int z) {}

	private final Map<String, Chunk> machines = new LinkedHashMap<>();

	public void add(String machineId, Chunk chunk) {
		if (machineId == null || machineId.isBlank()) throw new IllegalArgumentException("leeg machine-id");
		if (machines.putIfAbsent(machineId, chunk) != null) throw new IllegalArgumentException("dubbele machine: " + machineId);
	}

	public boolean remove(String machineId) { return machines.remove(machineId) != null; }

	public int size() { return machines.size(); }

	/** De machines die nu mogen tikken: alleen die in een geladen chunk, in volgorde van plaatsing. */
	public List<String> tickable(java.util.Set<Chunk> loaded) {
		List<String> out = new ArrayList<>();
		for (Map.Entry<String, Chunk> e : machines.entrySet()) {
			if (loaded.contains(e.getValue())) out.add(e.getKey());
		}
		return out;
	}
}
