package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

/** Lopende machinewerken (DR-802): een werk loopt door over meerdere ticks, stopt bij te weinig kracht en telt mee voor de machinegrens en de tijdsgrens per tick. */
public final class MachineJobs {
	/** Een afgerond werk. */
	public record Finished(String key, MachineRecipe recipe) {}

	private static final class Job {
		final MachineRecipe recipe;
		int left;

		Job(MachineRecipe recipe) {
			this.recipe = recipe;
			this.left = recipe.ticks();
		}
	}

	private final Map<String, Job> jobs = new LinkedHashMap<>();
	private final MachineBudget budget = new MachineBudget();

	/** Start een werk op een plek; false als daar al een werk loopt of de machinegrens bereikt is. */
	public boolean start(String key, MachineRecipe recipe) {
		if (jobs.containsKey(key) || !budget.tryActivate()) return false;
		jobs.put(key, new Job(recipe));
		return true;
	}

	public boolean running(String key) { return jobs.containsKey(key); }

	public int count() { return jobs.size(); }

	/** Voortgang van een werk in procenten, of -1 als er niets loopt. */
	public int percent(String key) {
		Job j = jobs.get(key);
		return j == null ? -1 : 100 * (j.recipe.ticks() - j.left) / j.recipe.ticks();
	}

	/** Eén tick: elk werk met genoeg kracht schuift een tick op, zolang de tijd niet op is; de rest wacht tot de volgende tick. */
	public List<Finished> tick(ToIntFunction<String> powerAt, TickBudget time) {
		List<Finished> klaar = new ArrayList<>();
		var it = jobs.entrySet().iterator();
		while (it.hasNext() && time.hasTime()) {
			var e = it.next();
			Job j = e.getValue();
			if (powerAt.applyAsInt(e.getKey()) < j.recipe.power()) continue;
			if (--j.left <= 0) {
				klaar.add(new Finished(e.getKey(), j.recipe));
				it.remove();
				budget.release();
			}
		}
		return klaar;
	}

	/** Beëindigt alle werken meteen en geeft ze terug, zodat bij afsluiten niets verloren gaat. */
	public List<Finished> finishAll() {
		List<Finished> klaar = new ArrayList<>();
		for (var e : jobs.entrySet()) klaar.add(new Finished(e.getKey(), e.getValue().recipe));
		jobs.clear();
		while (budget.active() > 0) budget.release();
		return klaar;
	}
}
