package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SteamBoilerTest {
	@Test
	void zonderBrandstofOfWaterGeenKracht() {
		SteamBoiler b = new SteamBoiler();
		assertEquals(0, b.tick());
		b.addWater(10);
		assertEquals(0, b.tick());
		SteamBoiler c = new SteamBoiler();
		c.addFuel(1);
		assertEquals(0, c.tick());
		assertEquals(1, c.fuel());
	}

	@Test
	void eenStukBrandstofBrandtVasteTijd() {
		SteamBoiler b = new SteamBoiler();
		b.addWater(SteamBoiler.MAX_WATER);
		b.addFuel(1);
		for (int i = 0; i < SteamBoiler.FUEL_TICKS; i++) assertEquals(SteamBoiler.POWER, b.tick());
		assertEquals(0, b.tick());
		assertEquals(0, b.fuel());
		assertEquals(0, SteamBoiler.MAX_WATER - SteamBoiler.FUEL_TICKS * SteamBoiler.WATER_PER_TICK - b.water());
	}

	@Test
	void waterOpIsKrachtWeg() {
		SteamBoiler b = new SteamBoiler();
		b.addWater(2);
		b.addFuel(1);
		assertEquals(SteamBoiler.POWER, b.tick());
		assertEquals(SteamBoiler.POWER, b.tick());
		assertEquals(0, b.tick());
		b.addWater(1);
		assertEquals(SteamBoiler.POWER, b.tick());
	}

	@Test
	void vakkenHebbenEenMaximum() {
		SteamBoiler b = new SteamBoiler();
		assertEquals(SteamBoiler.MAX_WATER, b.addWater(500));
		assertEquals(0, b.addWater(1));
		assertEquals(SteamBoiler.MAX_FUEL, b.addFuel(500));
		assertThrows(IllegalArgumentException.class, () -> b.addWater(0));
		assertThrows(IllegalArgumentException.class, () -> b.addFuel(-1));
	}
}
