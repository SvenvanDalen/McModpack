package com.drakenrijken.core;

import java.util.List;

/** Mijnkar-route (#146): de opbrengst staat vast op papier (laadgrootte per rondetijd), er hoeft geen kar te rijden als de speler weg is. */
public record CartRoute(int loadSize, int roundSeconds) {
	public static final int MAX_ACTIVE_CARTS = 4;

	public CartRoute {
		if (loadSize < 1) throw new IllegalArgumentException("laadgrootte minimaal 1: " + loadSize);
		if (roundSeconds < 1) throw new IllegalArgumentException("rondetijd minimaal 1 seconde: " + roundSeconds);
	}

	/** Aantal afgeleverd na een tijd: alleen volledige rondes tellen. */
	public long deliveredIn(long seconds) {
		return Math.max(0, seconds) / roundSeconds * loadSize;
	}

	/** Opbrengst per minuut, naar beneden afgerond. */
	public int perMinute() {
		return (int) ((long) loadSize * 60 / roundSeconds);
	}

	/** Totale opbrengst per minuut van alle routes; alleen de eerste {@link #MAX_ACTIVE_CARTS} tellen mee. */
	public static int totalPerMinute(List<CartRoute> routes) {
		int sum = 0;
		for (int i = 0; i < Math.min(MAX_ACTIVE_CARTS, routes.size()); i++) sum += routes.get(i).perMinute();
		return sum;
	}
}
