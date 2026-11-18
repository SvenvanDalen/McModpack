package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class BuildPowerTest {
	@Test
	void veertigBlokkenIsEenPunt() {
		assertEquals(0, BuildPower.fromBlocks(39));
		assertEquals(1, BuildPower.fromBlocks(40));
		assertEquals(25, BuildPower.fromBlocks(1000));
	}

	@Test
	void bouwenAlleenGeeftHooguitNiveauDrie() {
		assertEquals(BuildPower.MAX_POINTS, BuildPower.fromBlocks(10_000_000));
		assertEquals(3, BuildPower.level(List.of(), 10_000_000));
	}

	@Test
	void machinesEnBouwwerkTellenSamen() {
		assertEquals(0, BuildPower.level(List.of(), 0));
		assertEquals(1, BuildPower.level(List.of(5), 200));
		assertEquals(5, BuildPower.level(List.of(700), 12_000));
	}

	@Test
	void groterBouwwerkTrektMeerMensenEnMeerVijandenAan() {
		int klein = BuildPower.level(List.of(), 400);
		int groot = BuildPower.level(List.of(), 8_000);
		assertTrue(groot > klein);
		assertTrue(RealmVillagers.target(groot) >= RealmVillagers.target(klein));
		assertTrue(RealmRaids.waveSize(groot) > RealmRaids.waveSize(klein));
	}

	@Test
	void negatiefAantalBlokkenIsEenFout() {
		assertThrows(IllegalArgumentException.class, () -> BuildPower.fromBlocks(-1));
	}
}
