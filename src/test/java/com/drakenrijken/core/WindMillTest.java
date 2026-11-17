package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WindMillTest {
	@Test
	void geenKrachtOnderDeGrens() {
		assertEquals(0, WindMill.power(64, true));
		assertEquals(0, WindMill.power(-10, true));
	}

	@Test
	void geenKrachtZonderVrijeLucht() {
		assertEquals(0, WindMill.power(200, false));
	}

	@Test
	void maximumOpHoogte200EnDaarboven() {
		assertEquals(6, WindMill.power(200, true));
		assertEquals(6, WindMill.power(320, true));
	}

	@Test
	void groeitMetDeHoogteEnBlijftZwakkerDanWaterrad() {
		assertTrue(WindMill.power(130, true) < WindMill.power(180, true));
		assertTrue(WindMill.MAX_POWER < WaterWheel.power(WaterWheel.MAX_SIDES));
	}
}
