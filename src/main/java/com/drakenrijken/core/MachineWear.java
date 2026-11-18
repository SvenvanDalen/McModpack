package com.drakenrijken.core;

/** Slijtage van een machine (#149): na genoeg producten kan er een storing komen. Kost nooit spullen, alleen tijd; de dobbelworp komt van buiten zodat het testbaar blijft. */
public final class MachineWear {
	/** Aantal producten waarna storingen mogelijk worden (ongeveer 10 minuten werk). */
	public static final int THRESHOLD = 60;
	/** Kans per product (in procenten) bovenop de drempel, per product extra erbij. */
	public static final int CHANCE_STEP_PERCENT = 2;
	public static final int MAX_CHANCE_PERCENT = 40;

	private final boolean enabled;
	private int wear;
	private boolean broken;

	public MachineWear(boolean enabled) { this.enabled = enabled; }

	public int wear() { return wear; }

	public boolean broken() { return broken; }

	/** Kans op storing (0 tot 40 procent) bij de huidige slijtage. */
	public int chancePercent() {
		if (!enabled || wear < THRESHOLD) return 0;
		return Math.min(MAX_CHANCE_PERCENT, (wear - THRESHOLD + 1) * CHANCE_STEP_PERCENT);
	}

	/** Een product is gemaakt; roll is een getal in [0,1). Geeft true als de machine nu stukgaat. Een kapotte machine maakt niets. */
	public boolean onProduct(double roll) {
		if (!enabled || broken) return false;
		wear++;
		if (roll * 100 < chancePercent()) {
			broken = true;
			return true;
		}
		return false;
	}

	/** Herstel met de moersleutel: slijtage terug naar nul. Geeft false als er niets te herstellen was. */
	public boolean repair() {
		if (wear == 0 && !broken) return false;
		wear = 0;
		broken = false;
		return true;
	}

	/** Tekst voor de statusregel. */
	public String describe() { return broken ? "Storing" : "Slijtage " + wear; }
}
