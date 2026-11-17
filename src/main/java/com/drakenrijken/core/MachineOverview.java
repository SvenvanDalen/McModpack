package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Overzicht van machines (#148): een regel per machine, problemen eerst, hoogstens 64 regels. */
public final class MachineOverview {
	public static final int MAX_LINES = 64;

	private MachineOverview() {}

	/** Eén machine in het overzicht. */
	public record Entry(String name, RecipeMachine.State state, double progress) {}

	/** Alles behalve Bezig en Wacht op grondstoffen is een probleem. */
	public static boolean isProblem(RecipeMachine.State state) {
		return state != RecipeMachine.State.RUNNING && state != RecipeMachine.State.IDLE;
	}

	/** Regels als "naam: status", problemen eerst en daarna op naam; afgekapt op {@link #MAX_LINES}. */
	public static List<String> lines(List<Entry> entries) {
		List<Entry> sorted = new ArrayList<>(entries);
		sorted.sort(Comparator.comparing((Entry e) -> !isProblem(e.state())).thenComparing(Entry::name));
		List<String> out = new ArrayList<>();
		for (Entry e : sorted) {
			if (out.size() >= MAX_LINES) break;
			out.add(e.name() + ": " + MachineStatus.describe(e.state(), e.progress()));
		}
		return out;
	}

	/** Korte HUD-tekst, leeg als alles goed gaat. */
	public static String summary(List<Entry> entries) {
		long n = entries.stream().filter(e -> isProblem(e.state())).count();
		if (n == 0) return "";
		return n == 1 ? "1 machine heeft een probleem" : n + " machines hebben een probleem";
	}
}
