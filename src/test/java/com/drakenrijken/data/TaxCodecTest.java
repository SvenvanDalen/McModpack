package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.drakenrijken.core.PlayerData;
import com.mojang.serialization.JsonOps;
import org.junit.jupiter.api.Test;

class TaxCodecTest {
	@Test
	void laatsteBelastingdagBlijftBewaard() {
		PlayerData d = PlayerData.empty();
		d.setLastTaxDay(42);
		var json = PlayerDataCodec.CODEC.encodeStart(JsonOps.INSTANCE, d).getOrThrow();
		assertEquals(42, PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow().lastTaxDay());
	}

	@Test
	void oudeSavesZonderVeldStartenOpNul() {
		PlayerData r = PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE,
			com.google.gson.JsonParser.parseString("{\"level\":1,\"xp\":0}")).getOrThrow();
		assertEquals(0, r.lastTaxDay());
	}
}
