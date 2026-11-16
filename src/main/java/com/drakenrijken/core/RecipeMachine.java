package com.drakenrijken.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Een machine (bijv. de smederij) die recepten uitvoert. Heeft invoer- en uitvoervak, kost kracht
 * en telt alleen mee in het MachineBudget zolang hij echt werkt.
 */
public final class RecipeMachine {
	public static final int MAX_OUTPUT_PER_ITEM = 64;

	public enum State { IDLE, RUNNING, NO_POWER, OUTPUT_FULL, NO_BUDGET }

	private final String type;
	private final MachineRecipeBook book;
	private final MachineBudget budget;
	private final Map<String, Integer> inputs = new HashMap<>();
	private final Map<String, Integer> outputs = new HashMap<>();
	private MachineRecipe current;
	private int progress;
	private State state = State.IDLE;

	public RecipeMachine(String type, MachineRecipeBook book, MachineBudget budget) {
		this.type = type;
		this.book = book;
		this.budget = budget;
	}

	public State state() { return state; }

	/** Voortgang van 0.0 tot 1.0 van het lopende recept; 0 als de machine niets doet. */
	public double progress() {
		return current == null ? 0.0 : (double) progress / current.ticks();
	}

	public void addInput(String item, int count) {
		if (count < 1) throw new IllegalArgumentException("aantal moet minstens 1 zijn: " + count);
		inputs.merge(item, count, Integer::sum);
	}

	public int output(String item) { return outputs.getOrDefault(item, 0); }

	/** Haalt de uitvoer van een item uit het vak en geeft het aantal terug. */
	public int takeOutput(String item) {
		Integer n = outputs.remove(item);
		return n == null ? 0 : n;
	}

	/** Een tick met de beschikbare kracht. */
	public void tick(int powerAvailable) {
		if (current == null) {
			Optional<MachineRecipe> r = book.find(type, inputs);
			if (r.isEmpty()) {
				state = State.IDLE;
				return;
			}
			if (!budget.tryActivate()) {
				state = State.NO_BUDGET;
				return;
			}
			current = r.get();
			current.inputs().forEach((item, n) -> {
				int left = inputs.get(item) - n;
				if (left == 0) inputs.remove(item);
				else inputs.put(item, left);
			});
			progress = 0;
		}
		if (progress < current.ticks()) {
			if (powerAvailable < current.power()) {
				state = State.NO_POWER;
				return;
			}
			progress++;
		}
		if (progress >= current.ticks()) {
			if (output(current.output()) + current.outputCount() > MAX_OUTPUT_PER_ITEM) {
				state = State.OUTPUT_FULL;
				return;
			}
			outputs.merge(current.output(), current.outputCount(), Integer::sum);
			current = null;
			progress = 0;
			budget.release();
			state = State.IDLE;
			return;
		}
		state = State.RUNNING;
	}
}
