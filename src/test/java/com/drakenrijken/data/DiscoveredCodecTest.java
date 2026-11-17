package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.drakenrijken.core.PlayerData;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import org.junit.jupiter.api.Test;

class DiscoveredCodecTest {
	@Test
	void ontdekteKoninkrijkenBlijvenBewaard() {
		PlayerData d = PlayerData.empty();
		d.discovered().add("8,-24");
		d.discovered().add("136,40");
		JsonElement json = PlayerDataCodec.CODEC.encodeStart(JsonOps.INSTANCE, d).getOrThrow();
		PlayerData r = PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
		assertEquals(java.util.List.of("8,-24", "136,40"), java.util.List.copyOf(r.discovered()));
	}

	@Test
	void oudeSavesZonderVeldWerken() {
		PlayerData r = PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE,
			com.google.gson.JsonParser.parseString("{\"level\":1,\"xp\":0}")).getOrThrow();
		assertEquals(0, r.discovered().size());
	}
}
