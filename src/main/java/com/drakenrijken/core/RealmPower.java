package com.drakenrijken.core;

import java.util.List;

/** Telt de macht van actieve machines op, binnen de machinegrens. */
public final class RealmPower {
	private RealmPower() {}

	/** Telt hoogstens {@link MachineBudget#MAX_ACTIVE} machines mee. */
	public static int total(List<Integer> machinePower) {
		long sum = 0;
		int counted = 0;
		for (int p : machinePower) {
			if (p < 0) {
				throw new IllegalArgumentException("machtswaarde is negatief: " + p);
			}
			if (counted++ >= MachineBudget.MAX_ACTIVE) {
				break;
			}
			sum += p;
		}
		return (int) Math.min(sum, Integer.MAX_VALUE);
	}
}
