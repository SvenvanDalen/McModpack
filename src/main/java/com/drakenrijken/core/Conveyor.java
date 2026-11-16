package com.drakenrijken.core;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Eenvoudige transportband: een rij vakjes, elk met hooguit een item. Per tick schuift elk item
 * een vakje op. Het laatste vakje levert het item af aan een ontvanger; weigert die, dan blijft het staan
 * en de band achter het item loopt vol (niets raakt kwijt, niets wordt verdubbeld).
 */
public final class Conveyor {
	public static final int MAX_LENGTH = 32;

	private final String[] slots;

	public Conveyor(int length) {
		if (length < 1 || length > MAX_LENGTH) throw new IllegalArgumentException("lengte buiten 1.." + MAX_LENGTH + ": " + length);
		this.slots = new String[length];
	}

	public int length() { return slots.length; }

	/** Zet een item op het begin van de band; false als dat vakje bezet is. */
	public boolean insert(String item) {
		if (item == null || item.isBlank()) throw new IllegalArgumentException("leeg item");
		if (slots[0] != null) return false;
		slots[0] = item;
		return true;
	}

	/** Eén tik: eerst levert het laatste vakje af (als de ontvanger het accepteert), dan schuift alles op. */
	public void tick(Predicate<String> receiver) {
		int last = slots.length - 1;
		if (slots[last] != null && receiver.test(slots[last])) slots[last] = null;
		for (int i = last; i > 0; i--) {
			if (slots[i] == null && slots[i - 1] != null) {
				slots[i] = slots[i - 1];
				slots[i - 1] = null;
			}
		}
	}

	public Optional<String> at(int slot) { return Optional.ofNullable(slots[slot]); }

	public int count() {
		int n = 0;
		for (String s : slots) if (s != null) n++;
		return n;
	}
}
