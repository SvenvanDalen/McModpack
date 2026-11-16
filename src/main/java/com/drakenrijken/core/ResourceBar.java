package com.drakenrijken.core;

/** Mana (magiër) of uithouding (ridder, schurk): een balk met maximum en herstel. */
public final class ResourceBar {
	private final int max;
	private int value;

	public ResourceBar(int max) {
		if (max <= 0) throw new IllegalArgumentException("maximum moet positief zijn: " + max);
		this.max = max;
		this.value = max;
	}

	public int value() { return value; }

	public int max() { return max; }

	/** Gebruikt de kosten als er genoeg is; anders verandert er niets en komt false terug. */
	public boolean spend(int cost) {
		if (cost < 0) throw new IllegalArgumentException("kosten zijn negatief: " + cost);
		if (cost > value) return false;
		value -= cost;
		return true;
	}

	public void regen(int amount) {
		if (amount < 0) throw new IllegalArgumentException("herstel is negatief: " + amount);
		value = (int) Math.min((long) value + amount, max);
	}

	/** De balk die bij een klasse hoort: mana voor magiër, uithouding voor de rest. */
	public static ResourceBar forClass(PlayerClass c) {
		return new ResourceBar(c == PlayerClass.MAGIER ? 100 : 80);
	}
}
