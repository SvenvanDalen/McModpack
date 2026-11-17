package com.drakenrijken.core;

/** Beroepen van dorpelingen (#152). Opbrengst per dag per dorpeling; waarden zijn eerste inschattingen, nog niet door de Economy Designer nagekeken. */
public enum Profession {
	BOER("Boer", 1),
	MIJNWERKER("Mijnwerker", 2),
	SMID("Smid", 2),
	WACHTER("Wachter", 0);

	private final String naam;
	private final int dailyYield;

	Profession(String naam, int dailyYield) {
		this.naam = naam;
		this.dailyYield = dailyYield;
	}

	public String naam() { return naam; }

	public int dailyYield() { return dailyYield; }

	/** Verdeling van het aantal dorpelingen over de beroepen, in volgorde van de enum; de rest gaat naar de eerste. */
	public static int[] assign(int villagers) {
		int n = Math.max(0, villagers);
		int[] r = new int[values().length];
		int each = n / r.length;
		java.util.Arrays.fill(r, each);
		r[0] += n - each * r.length;
		return r;
	}

	/** Opbrengst per dag van een rijk op dit niveau, op papier berekend. */
	public static int dailyYield(int level) {
		int[] r = assign(RealmVillagers.target(level));
		int sum = 0;
		for (Profession p : values()) sum += r[p.ordinal()] * p.dailyYield;
		return sum;
	}
}
