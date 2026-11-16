package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RecipeMachineTest {
	private final MachineBudget budget = new MachineBudget();
	private final MachineRecipeBook book = new MachineRecipeBook(List.of(
		new MachineRecipe("ijzer", "smederij", Map.of("erts", 1), "staaf", 2, 3, 2)));

	private RecipeMachine smederij() {
		return new RecipeMachine("smederij", book, budget);
	}

	@Test
	void zonderInvoerBlijftHijLeegEnTeltNietMee() {
		RecipeMachine m = smederij();
		m.tick(10);
		assertEquals(RecipeMachine.State.IDLE, m.state());
		assertEquals(0, budget.active());
	}

	@Test
	void maaktUitErtsStavenNaDeDuur() {
		RecipeMachine m = smederij();
		m.addInput("erts", 1);
		m.tick(2);
		assertEquals(RecipeMachine.State.RUNNING, m.state());
		assertEquals(1, budget.active());
		m.tick(2);
		m.tick(2);
		assertEquals(RecipeMachine.State.IDLE, m.state());
		assertEquals(0, budget.active());
		assertEquals(2, m.takeOutput("staaf"));
		assertEquals(0, m.takeOutput("staaf"));
	}

	@Test
	void teWeinigKrachtPauzeertDeVoortgang() {
		RecipeMachine m = smederij();
		m.addInput("erts", 1);
		m.tick(2);
		double voor = m.progress();
		m.tick(1);
		assertEquals(RecipeMachine.State.NO_POWER, m.state());
		assertEquals(voor, m.progress());
		m.tick(2);
		assertEquals(RecipeMachine.State.RUNNING, m.state());
	}

	@Test
	void volBudgetStartNietEnGaatDoorNaVrijgeven() {
		for (int i = 0; i < MachineBudget.MAX_ACTIVE; i++) budget.tryActivate();
		RecipeMachine m = smederij();
		m.addInput("erts", 1);
		m.tick(2);
		assertEquals(RecipeMachine.State.NO_BUDGET, m.state());
		budget.release();
		m.tick(2);
		assertEquals(RecipeMachine.State.RUNNING, m.state());
	}

	@Test
	void volUitvoervakHoudtDeMachineTegen() {
		RecipeMachine m = smederij();
		for (int i = 0; i < 40; i++) {
			m.addInput("erts", 1);
			for (int t = 0; t < 3; t++) m.tick(2);
		}
		assertEquals(64, m.output("staaf"));
		m.addInput("erts", 1);
		for (int t = 0; t < 3; t++) m.tick(2);
		assertEquals(RecipeMachine.State.OUTPUT_FULL, m.state());
		assertEquals(64, m.takeOutput("staaf"));
		m.tick(2);
		assertEquals(2, m.output("staaf"));
	}

	@Test
	void ongeldigeInvoerWordtAfgewezen() {
		assertThrows(IllegalArgumentException.class, () -> smederij().addInput("erts", 0));
	}
}
