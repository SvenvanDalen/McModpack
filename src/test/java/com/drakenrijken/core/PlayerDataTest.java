package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlayerDataTest {
	@Test
	void legeSpelerHeeftGeenKlasseEnStartOpLevel1() {
		PlayerData d = PlayerData.empty();
		assertTrue(d.playerClass().isEmpty());
		assertEquals(1, d.progress().level());
	}

	@Test
	void legeSpelersDelenGeenToestand() {
		assertNotSame(PlayerData.empty().progress(), PlayerData.empty().progress());
	}
}
