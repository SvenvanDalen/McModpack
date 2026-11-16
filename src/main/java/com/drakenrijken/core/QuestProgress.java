package com.drakenrijken.core;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** Voortgang per quest van een speler. */
public final class QuestProgress {
	private final Map<String, Integer> counts = new LinkedHashMap<>();
	private final Set<String> completed = new LinkedHashSet<>();

	public int progress(String id) { return counts.getOrDefault(id, 0); }

	public boolean isCompleted(String id) { return completed.contains(id); }

	public Map<String, Integer> counts() { return counts; }

	public Set<String> completed() { return completed; }
}
