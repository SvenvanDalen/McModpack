package com.drakenrijken.core;

import java.util.List;

/** Verhaal van een ruïne (#158): aanwijzingen die je in volgorde vindt en een eindvondst. Maximaal 12x12 groot. */
public final class RuinStory {
	public static final int MAX_SIZE = 12;

	private final List<String> clues;
	private final String findLootTable;
	private int found;

	public RuinStory(List<String> clues, String findLootTable) {
		if (clues.isEmpty()) throw new IllegalArgumentException("een ruïne heeft minstens één aanwijzing");
		this.clues = List.copyOf(clues);
		this.findLootTable = findLootTable;
	}

	public int found() { return found; }

	public boolean complete() { return found >= clues.size(); }

	/** De volgende aanwijzing lezen; null als alles al gevonden is. */
	public String nextClue() { return complete() ? null : clues.get(found++); }

	/** De vondst (naam van de buittabel) pas als alle aanwijzingen gelezen zijn, anders null. */
	public String loot() { return complete() ? findLootTable : null; }

	/** Past het plan binnen 12x12 grondoppervlak? */
	public static boolean fits(Blueprint plan) {
		if (plan.placements().isEmpty()) return true;
		int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
		for (Blueprint.Placement p : plan.placements()) {
			minX = Math.min(minX, p.x());
			maxX = Math.max(maxX, p.x());
			minZ = Math.min(minZ, p.z());
			maxZ = Math.max(maxZ, p.z());
		}
		return maxX - minX + 1 <= MAX_SIZE && maxZ - minZ + 1 <= MAX_SIZE;
	}
}
