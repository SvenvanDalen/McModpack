package com.drakenrijken.core;

/** Bliksemvanger en accu (#151): een bliksemslag geeft lading, machines putten eruit. Verandert alleen bij een gebeurtenis, nooit per tick. */
public final class LightningRod {
	public static final int CHARGE_PER_STRIKE = 100;
	public static final int CAPACITY = 500;

	private int charge;

	public int charge() { return charge; }

	/** Een bliksemslag op de vanger; geeft de lading die er echt bij kwam (niet meer dan er past). */
	public int strike() {
		int erbij = Math.min(CHARGE_PER_STRIKE, CAPACITY - charge);
		charge += erbij;
		return erbij;
	}

	/** Neemt lading op voor een machine; alles of niets, zodat een halve taak niet kan. */
	public boolean draw(int amount) {
		if (amount < 0 || amount > charge) return false;
		charge -= amount;
		return true;
	}
}
