package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HudLinesTest {
	@Test
	void alleOnderdelen() {
		var b = ResourceBar.forClass(PlayerClass.MAGIER);
		b.spend(30);
		assertEquals("Quest: hout: 1/5\nMana 70/100\nRijk niveau 2", HudLines.build("hout: 1/5", PlayerClass.MAGIER, b, 2));
	}

	@Test
	void legeOnderdelenVallenWeg() {
		assertEquals("", HudLines.build("", null, null, 0));
		assertEquals("Uithouding 80/80", HudLines.build("", PlayerClass.RIDDER, ResourceBar.forClass(PlayerClass.RIDDER), 0));
	}

	@Test
	void onmyojiToontManaSamoeraiUithouding() {
		assertEquals("Mana 100/100", HudLines.build("", PlayerClass.ONMYOJI, ResourceBar.forClass(PlayerClass.ONMYOJI), 0));
		assertEquals("Uithouding 80/80", HudLines.build("", PlayerClass.SAMOERAI, ResourceBar.forClass(PlayerClass.SAMOERAI), 0));
	}
}
