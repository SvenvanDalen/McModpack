package com.drakenrijken.core;

import java.util.Map;

/**
 * Een machinerecept: een machine maakt uit invoer-items (id naar aantal) een uitvoer-item.
 * ticks is de duur; power is de kracht die de machine daarvoor nodig heeft.
 */
public record MachineRecipe(String id, String machine, Map<String, Integer> inputs, String output, int outputCount,
	int ticks, int power) {
	public MachineRecipe {
		if (id == null || id.isBlank()) throw new IllegalArgumentException("recept zonder id");
		if (machine == null || machine.isBlank()) throw new IllegalArgumentException("recept zonder machine: " + id);
		if (output == null || output.isBlank()) throw new IllegalArgumentException("recept zonder uitvoer: " + id);
		if (inputs == null || inputs.isEmpty()) throw new IllegalArgumentException("recept zonder invoer: " + id);
		inputs.forEach((k, v) -> {
			if (v == null || v < 1) throw new IllegalArgumentException("invoeraantal moet minstens 1 zijn: " + id + " " + k);
		});
		if (outputCount < 1) throw new IllegalArgumentException("uitvoeraantal moet minstens 1 zijn: " + id);
		if (ticks < 1) throw new IllegalArgumentException("duur moet minstens 1 tick zijn: " + id);
		if (power < 0) throw new IllegalArgumentException("kracht is negatief: " + id);
		inputs = Map.copyOf(inputs);
	}
}
