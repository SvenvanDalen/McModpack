package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StructureQueueTest {
	@Test
	void perTickHooguitDeGrens() {
		StructureQueue q = new StructureQueue();
		for (int i = 0; i < 50; i++) q.enqueue(new StructureQueue.Request("dorp", i, 0));
		int totaal = 0;
		for (int tick = 0; tick < 10; tick++) {
			int n = q.nextTick().size();
			assertTrue(n <= StructureQueue.PER_TICK);
			totaal += n;
		}
		assertEquals(10 * StructureQueue.PER_TICK, totaal);
		assertEquals(50 - totaal, q.pending());
	}

	@Test
	void volgordeBlijftBehouden() {
		StructureQueue q = new StructureQueue();
		q.enqueue(new StructureQueue.Request("dorp", 1, 1));
		q.enqueue(new StructureQueue.Request("burcht", 2, 2));
		assertEquals("dorp", q.nextTick().get(0).structure());
		assertEquals("burcht", q.nextTick().get(0).structure());
		assertTrue(q.nextTick().isEmpty());
	}

	@Test
	void dezelfdePlekKomtMaarEenKeer() {
		StructureQueue q = new StructureQueue();
		StructureQueue.Request r = new StructureQueue.Request("dorp", 3, 4);
		assertTrue(q.enqueue(r));
		assertFalse(q.enqueue(r));
		q.nextTick();
		assertFalse(q.enqueue(r), "ook na bouwen niet opnieuw");
	}
}
