package com.drakenrijken.core;

import java.util.List;
import java.util.Map;

/** Korte statusteksten voor machines (DR-410): voortgang van een lopend recept en de reden als er niets gebeurt. */
public final class MachineStatus {
	private MachineStatus() {}

	/** Tekst bij een status en voortgang (0.0 tot 1.0) van een {@link RecipeMachine}. */
	public static String describe(RecipeMachine.State state, double progress) {
		int pct = (int) Math.round(Math.max(0.0, Math.min(1.0, progress)) * 100);
		return switch (state) {
			case IDLE -> "Wacht op grondstoffen";
			case RUNNING -> "Bezig: " + pct + "%";
			case NO_POWER -> "Te weinig kracht (" + pct + "%)";
			case OUTPUT_FULL -> "Uitvoer vol";
			case NO_BUDGET -> "Te veel machines actief";
		};
	}

	/** Legt uit waarom er geen recept uitgevoerd kan worden, bijvoorbeeld wat er ontbreekt of hoeveel kracht er nodig is. */
	public static String whyNot(List<MachineRecipe> recipes, Map<String, Integer> have, int power) {
		if (recipes.isEmpty()) return "Deze machine heeft geen recepten.";
		MachineRecipe best = null;
		int bestMissing = Integer.MAX_VALUE;
		for (MachineRecipe r : recipes) {
			int missing = 0;
			for (var e : r.inputs().entrySet()) missing += Math.max(0, e.getValue() - have.getOrDefault(e.getKey(), 0));
			if (missing < bestMissing) {
				bestMissing = missing;
				best = r;
			}
		}
		if (bestMissing > 0) {
			StringBuilder sb = new StringBuilder("Je mist nog: ");
			boolean first = true;
			for (var e : best.inputs().entrySet()) {
				int tekort = e.getValue() - have.getOrDefault(e.getKey(), 0);
				if (tekort <= 0) continue;
				if (!first) sb.append(", ");
				sb.append(tekort).append("x ").append(e.getKey());
				first = false;
			}
			return sb.toString();
		}
		return "Te weinig kracht: " + best.power() + " nodig, " + power + " beschikbaar.";
	}
}
