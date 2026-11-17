package com.drakenrijken.core;

/** Leiding voor vloeistoffen (#150, eerste stap): een doorvoer per tick en een maximale lengte. Waarden zijn eerste inschattingen; lekken horen er nog niet bij. */
public final class Pipe {
	public static final int MAX_LENGTH = PowerNetwork.MAX_SHAFTS;
	public static final int THROUGHPUT_PER_TICK = 10;

	private Pipe() {}

	/** Mag een leiding van deze lengte (aantal buisblokken) bestaan? */
	public static boolean validLength(int length) {
		return length >= 1 && length <= MAX_LENGTH;
	}

	/** Hoeveel er per tick aankomt: het minimum van wat de bron levert, wat de bestemming aanneemt en de doorvoer. Ongeldige lengte geeft 0. */
	public static int delivered(int length, int supply, int demand) {
		if (!validLength(length)) return 0;
		return Math.max(0, Math.min(THROUGHPUT_PER_TICK, Math.min(supply, demand)));
	}
}
