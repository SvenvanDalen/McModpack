package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Alle machinerecepten; zoekt het recept dat bij een machine en een stapel invoer past. */
public final class MachineRecipeBook {
	private final List<MachineRecipe> recipes;

	public MachineRecipeBook(List<MachineRecipe> recipes) {
		Set<String> ids = new HashSet<>();
		for (MachineRecipe r : recipes) {
			if (!ids.add(r.id())) throw new IllegalArgumentException("dubbel recept: " + r.id());
		}
		this.recipes = List.copyOf(recipes);
	}

	public List<MachineRecipe> all() { return recipes; }

	public List<MachineRecipe> forMachine(String machine) {
		List<MachineRecipe> out = new ArrayList<>();
		for (MachineRecipe r : recipes) if (r.machine().equals(machine)) out.add(r);
		return out;
	}

	/** Eerste recept van deze machine waarvan alle invoer in available (item-id naar aantal) aanwezig is. */
	public Optional<MachineRecipe> find(String machine, Map<String, Integer> available) {
		for (MachineRecipe r : recipes) {
			if (!r.machine().equals(machine)) continue;
			boolean ok = true;
			for (var e : r.inputs().entrySet()) {
				if (available.getOrDefault(e.getKey(), 0) < e.getValue()) {
					ok = false;
					break;
				}
			}
			if (ok) return Optional.of(r);
		}
		return Optional.empty();
	}
}
