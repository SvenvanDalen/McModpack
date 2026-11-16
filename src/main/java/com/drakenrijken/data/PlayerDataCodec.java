package com.drakenrijken.data;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.PlayerData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Opslagformaat van PlayerData; staat buiten core/ omdat Codec een Mojang-klasse is. */
public final class PlayerDataCodec {
	private PlayerDataCodec() {}

	private record Raw(Optional<String> playerClass, int level, int xp, List<String> unlocked,
		Map<String, Integer> questCounts, List<String> questsDone) {}

	private static final Codec<Raw> RAW = RecordCodecBuilder.create(i -> i.group(
		Codec.STRING.optionalFieldOf("class").forGetter(Raw::playerClass),
		Codec.INT.fieldOf("level").forGetter(Raw::level),
		Codec.INT.fieldOf("xp").forGetter(Raw::xp),
		Codec.STRING.listOf().optionalFieldOf("unlocked", List.of()).forGetter(Raw::unlocked),
		Codec.unboundedMap(Codec.STRING, Codec.INT).optionalFieldOf("questCounts", Map.of()).forGetter(Raw::questCounts),
		Codec.STRING.listOf().optionalFieldOf("questsDone", List.of()).forGetter(Raw::questsDone)
	).apply(i, Raw::new));

	public static final Codec<PlayerData> CODEC = RAW.flatXmap(PlayerDataCodec::fromRaw, d -> DataResult.success(toRaw(d)));

	private static Raw toRaw(PlayerData d) {
		return new Raw(d.playerClass().map(PlayerClass::name), d.progress().level(), d.progress().xp(),
			new ArrayList<>(d.progress().unlocked()), new LinkedHashMap<>(d.quests().counts()),
			new ArrayList<>(d.quests().completed()));
	}

	private static DataResult<PlayerData> fromRaw(Raw r) {
		PlayerData d = PlayerData.empty();
		try {
			if (r.playerClass().isPresent()) {
				d.setPlayerClass(PlayerClass.valueOf(r.playerClass().get()));
			}
			d.progress().restore(r.level(), r.xp());
		} catch (IllegalArgumentException e) {
			return DataResult.error(() -> "ongeldige spelerdata: " + e.getMessage());
		}
		d.progress().unlocked().addAll(r.unlocked());
		d.quests().counts().putAll(r.questCounts());
		d.quests().completed().addAll(r.questsDone());
		return DataResult.success(d);
	}
}
