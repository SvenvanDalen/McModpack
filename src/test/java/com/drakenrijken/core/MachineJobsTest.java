package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MachineJobsTest {
	private static MachineRecipe recipe(int ticks, int power) {
		return new MachineRecipe("r", "smederij", Map.of("a", 1), "b", 1, ticks, power);
	}

	private static TickBudget ruim() { return new TickBudget(1_000_000_000L, System::nanoTime); }

	@Test
	void werkLooptTotHetKlaarIs() {
		var jobs = new MachineJobs();
		assertTrue(jobs.start("x", recipe(3, 1)));
		assertFalse(jobs.start("x", recipe(3, 1)));
		assertTrue(jobs.tick(k -> 2, ruim()).isEmpty());
		assertEquals(33, jobs.percent("x"));
		assertTrue(jobs.tick(k -> 2, ruim()).isEmpty());
		var klaar = jobs.tick(k -> 2, ruim());
		assertEquals(1, klaar.size());
		assertEquals("x", klaar.get(0).key());
		assertFalse(jobs.running("x"));
		assertEquals(-1, jobs.percent("x"));
	}

	@Test
	void zonderKrachtWachtHetWerk() {
		var jobs = new MachineJobs();
		jobs.start("x", recipe(1, 3));
		assertTrue(jobs.tick(k -> 2, ruim()).isEmpty());
		assertTrue(jobs.running("x"));
		assertEquals(1, jobs.tick(k -> 3, ruim()).size());
	}

	@Test
	void grensOpAantalActieveMachines() {
		var jobs = new MachineJobs();
		for (int i = 0; i < MachineBudget.MAX_ACTIVE; i++) assertTrue(jobs.start("m" + i, recipe(5, 0)));
		assertFalse(jobs.start("extra", recipe(5, 0)));
		assertEquals(MachineBudget.MAX_ACTIVE, jobs.count());
	}

	@Test
	void opTijdWachtenDeRestendeWerken() {
		var jobs = new MachineJobs();
		jobs.start("x", recipe(1, 0));
		long[] t = {0};
		var weinig = new TickBudget(10, () -> t[0] += 100);
		assertTrue(jobs.tick(k -> 0, weinig).isEmpty());
		assertTrue(jobs.running("x"));
	}

	@Test
	void afsluitenGeeftAllesTerug() {
		var jobs = new MachineJobs();
		jobs.start("x", recipe(50, 0));
		jobs.start("y", recipe(50, 0));
		assertEquals(2, jobs.finishAll().size());
		assertEquals(0, jobs.count());
		assertTrue(jobs.start("x", recipe(50, 0)));
	}
}
