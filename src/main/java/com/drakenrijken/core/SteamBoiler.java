package com.drakenrijken.core;

/**
 * Stoomketel (tijdperk 2): brandstof wordt stoom, stoom wordt kracht.
 * Heeft water en brandstof nodig. Waarden zijn eerste inschattingen.
 */
public final class SteamBoiler {
	public static final int MAX_WATER = 100;
	public static final int MAX_FUEL = 64;
	public static final int FUEL_TICKS = 100;
	public static final int WATER_PER_TICK = 1;
	public static final int POWER = 6;

	private int water;
	private int fuel;
	private int burnLeft;

	public int water() { return water; }

	public int fuel() { return fuel; }

	/** Voegt water toe tot het maximum en geeft terug hoeveel er echt in ging. */
	public int addWater(int amount) {
		if (amount < 1) throw new IllegalArgumentException("aantal moet minstens 1 zijn: " + amount);
		int added = Math.min(amount, MAX_WATER - water);
		water += added;
		return added;
	}

	/** Voegt brandstof toe tot het maximum en geeft terug hoeveel er echt in ging. */
	public int addFuel(int amount) {
		if (amount < 1) throw new IllegalArgumentException("aantal moet minstens 1 zijn: " + amount);
		int added = Math.min(amount, MAX_FUEL - fuel);
		fuel += added;
		return added;
	}

	/** Een tick; geeft de kracht die de ketel nu levert (0 zonder water of brandstof). */
	public int tick() {
		if (water < WATER_PER_TICK) return 0;
		if (burnLeft == 0) {
			if (fuel == 0) return 0;
			fuel--;
			burnLeft = FUEL_TICKS;
		}
		burnLeft--;
		water -= WATER_PER_TICK;
		return POWER;
	}
}
