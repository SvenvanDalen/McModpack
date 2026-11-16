package com.drakenrijken.core;

/** Harde grens op het aantal eigen NPC's en vijanden tegelijk in beeld (DR-803). */
public final class EntityBudget {
	public static final int MAX_NPCS = 16;
	public static final int MAX_ENEMIES = 24;

	public enum Kind { NPC, ENEMY }

	private int npcs;
	private int enemies;

	/** Probeert een entiteit toe te voegen; false als de grens voor die soort bereikt is. */
	public boolean trySpawn(Kind kind) {
		if (kind == Kind.NPC) {
			if (npcs >= MAX_NPCS) return false;
			npcs++;
		} else {
			if (enemies >= MAX_ENEMIES) return false;
			enemies++;
		}
		return true;
	}

	/** Meldt dat een entiteit verdwijnt; zakt nooit onder nul. */
	public void despawn(Kind kind) {
		if (kind == Kind.NPC) {
			if (npcs > 0) npcs--;
		} else if (enemies > 0) {
			enemies--;
		}
	}

	public int count(Kind kind) { return kind == Kind.NPC ? npcs : enemies; }
}
