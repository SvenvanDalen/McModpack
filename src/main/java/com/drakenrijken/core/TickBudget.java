package com.drakenrijken.core;

import java.util.function.LongSupplier;

/** Maximumtijd per tick voor een onderdeel (machines, rijk); werk stopt zodra de tijd op is (DR-802). */
public final class TickBudget {
	public static final long MACHINES_NANOS = 2_000_000L;
	public static final long REALM_NANOS = 1_000_000L;

	private final long limitNanos;
	private final LongSupplier clock;
	private long start;

	public TickBudget(long limitNanos, LongSupplier clock) {
		if (limitNanos <= 0) throw new IllegalArgumentException("limitNanos <= 0");
		this.limitNanos = limitNanos;
		this.clock = clock;
		this.start = clock.getAsLong();
	}

	public static TickBudget machines() { return new TickBudget(MACHINES_NANOS, System::nanoTime); }

	public static TickBudget realm() { return new TickBudget(REALM_NANOS, System::nanoTime); }

	/** Begin van een nieuwe tick. */
	public void begin() { start = clock.getAsLong(); }

	/** True zolang er tijd over is in deze tick. */
	public boolean hasTime() { return clock.getAsLong() - start < limitNanos; }
}
