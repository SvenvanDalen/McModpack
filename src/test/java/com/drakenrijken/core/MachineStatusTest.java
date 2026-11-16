package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MachineStatusTest {
	private static final MachineRecipe IJZER = new MachineRecipe("ijzer", "smederij", Map.of("raw_iron", 1, "coal", 1), "iron_ingot", 1, 40, 2);

	@Test
	void beschrijftStatussen() {
		assertEquals("Bezig: 50%", MachineStatus.describe(RecipeMachine.State.RUNNING, 0.5));
		assertEquals("Te weinig kracht (25%)", MachineStatus.describe(RecipeMachine.State.NO_POWER, 0.25));
		assertEquals("Wacht op grondstoffen", MachineStatus.describe(RecipeMachine.State.IDLE, 0));
		assertEquals("Bezig: 100%", MachineStatus.describe(RecipeMachine.State.RUNNING, 7.0));
	}

	@Test
	void legtUitWatErMist() {
		assertEquals("Je mist nog: 1x coal", MachineStatus.whyNot(List.of(IJZER), Map.of("raw_iron", 1), 5));
		assertTrue(MachineStatus.whyNot(List.of(IJZER), Map.of("raw_iron", 1, "coal", 1), 1).startsWith("Te weinig kracht: 2 nodig, 1"));
		assertEquals("Deze machine heeft geen recepten.", MachineStatus.whyNot(List.of(), Map.of(), 5));
	}
}
