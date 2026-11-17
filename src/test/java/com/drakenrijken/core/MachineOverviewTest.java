package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MachineOverviewTest {
	@Test
	void problemenStaanEerst() {
		var lines = MachineOverview.lines(List.of(
			new MachineOverview.Entry("Smelter", RecipeMachine.State.RUNNING, 0.5),
			new MachineOverview.Entry("Molen", RecipeMachine.State.NO_POWER, 0.2)));
		assertEquals("Molen: Te weinig kracht (20%)", lines.get(0));
		assertEquals("Smelter: Bezig: 50%", lines.get(1));
	}

	@Test
	void begrensdOp64Regels() {
		List<MachineOverview.Entry> alle = new ArrayList<>();
		for (int i = 0; i < 100; i++) alle.add(new MachineOverview.Entry("M" + i, RecipeMachine.State.IDLE, 0));
		assertEquals(64, MachineOverview.lines(alle).size());
	}

	@Test
	void samenvattingTeltProblemen() {
		assertEquals("", MachineOverview.summary(List.of(new MachineOverview.Entry("A", RecipeMachine.State.RUNNING, 0))));
		var twee = List.of(
			new MachineOverview.Entry("A", RecipeMachine.State.OUTPUT_FULL, 0),
			new MachineOverview.Entry("B", RecipeMachine.State.NO_BUDGET, 0));
		assertEquals("2 machines hebben een probleem", MachineOverview.summary(twee));
		assertTrue(MachineOverview.summary(twee.subList(0, 1)).startsWith("1 machine "));
	}
}
