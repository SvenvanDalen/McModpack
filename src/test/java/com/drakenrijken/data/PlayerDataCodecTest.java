package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.PlayerData;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import org.junit.jupiter.api.Test;

class PlayerDataCodecTest {
	private static PlayerData roundtrip(PlayerData d) {
		JsonElement json = PlayerDataCodec.CODEC.encodeStart(JsonOps.INSTANCE, d).getOrThrow();
		return PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
	}

	@Test
	void roundtripGeeftGelijkeData() {
		PlayerData d = PlayerData.empty();
		d.setPlayerClass(PlayerClass.MAGIER);
		d.progress().unlocked().add("vuurbal");
		d.progress().restore(7, 40);
		d.quests().counts().put("hout", 3);
		d.quests().completed().add("dorp");
		d.setRealmPower(260);
		PlayerData r = roundtrip(d);
		assertEquals(260, r.realmPower());
		assertEquals(PlayerClass.MAGIER, r.playerClass().orElseThrow());
		assertEquals(7, r.progress().level());
		assertEquals(40, r.progress().xp());
		assertEquals(d.progress().unlocked(), r.progress().unlocked());
		assertEquals(3, r.quests().progress("hout"));
		assertTrue(r.quests().isCompleted("dorp"));
	}

	@Test
	void legeDataBlijftLeeg() {
		PlayerData r = roundtrip(PlayerData.empty());
		assertTrue(r.playerClass().isEmpty());
		assertEquals(1, r.progress().level());
	}

	@Test
	void ongeldigLevelWordtAfgewezenInPlaatsVanCrash() {
		var json = new com.google.gson.JsonObject();
		json.addProperty("level", 99);
		json.addProperty("xp", 0);
		var res = PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE, json);
		assertTrue(res.error().isPresent());
	}

	@Test
	void negatieveMachtInOpslagWordtAfgewezen() {
		var json = new com.google.gson.JsonObject();
		json.addProperty("level", 1);
		json.addProperty("xp", 0);
		json.addProperty("realmPower", -5);
		assertTrue(PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE, json).error().isPresent());
	}

	@Test
	void bonuspuntenBlijvenBewaard() {
		PlayerData d = PlayerData.empty();
		d.progress().addBonusPoints(3);
		assertEquals(3, roundtrip(d).progress().bonusPoints());
	}

	@Test
	void oudeOpslagZonderMachtGeeftNul() {
		var json = new com.google.gson.JsonObject();
		json.addProperty("level", 1);
		json.addProperty("xp", 0);
		assertEquals(0, PlayerDataCodec.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow().realmPower());
	}
}
