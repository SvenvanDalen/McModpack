package com.drakenrijken.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Geleidelijke wereldgeneratie: structuren komen in een wachtrij en per tick wordt er hooguit
 * een beperkt aantal gebouwd, zodat er geen tick-piek ontstaat. Elke plek komt maar één keer in de rij.
 */
public final class StructureQueue {
	public static final int PER_TICK = 1;

	public record Request(String structure, int chunkX, int chunkZ) {}

	private final ArrayDeque<Request> queue = new ArrayDeque<>();
	private final Set<Request> known = new HashSet<>();

	/** Zet een structuur in de rij; false als dezelfde aanvraag al bestaat (wachtend of gebouwd). */
	public boolean enqueue(Request r) {
		if (!known.add(r)) return false;
		queue.add(r);
		return true;
	}

	public int pending() { return queue.size(); }

	/** Geeft de structuren die deze tick gebouwd mogen worden (hooguit PER_TICK). */
	public List<Request> nextTick() {
		List<Request> out = new ArrayList<>();
		while (out.size() < PER_TICK && !queue.isEmpty()) out.add(queue.poll());
		return out;
	}
}
