package com.drakenrijken.core;

/** Hoeveel dorpelingen er bij moeten (DR-503): tot de rijksgrens, maar nooit boven de NPC-grens (DR-803). */
public final class RealmVillagers {
	private RealmVillagers() {}

	public static int target(int level) {
		return Math.min(RealmEffects.villagerCap(level), EntityBudget.MAX_NPCS);
	}

	/** Aantal nieuwe dorpelingen; nooit negatief. */
	public static int toSpawn(int level, int existing) {
		return Math.max(0, target(level) - Math.max(0, existing));
	}
}
