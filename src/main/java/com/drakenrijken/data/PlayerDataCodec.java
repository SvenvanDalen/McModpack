package com.drakenrijken.data;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.PlayerData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Opslagformaat van PlayerData; staat buiten core/ omdat Codec een Mojang-klasse is. */
public final class PlayerDataCodec {
	private PlayerDataCodec() {}

	private record Raw(Optional<String> playerClass, int level, int xp, List<String> unlocked) {}

	private static final Codec<Raw> RAW = RecordCodecBuilder.create(i -> i.group(
		Codec.STRING.optionalFieldOf("class").forGetter(Raw::playerClass),
		Codec.INT.fieldOf("level").forGetter(Raw::level),
		Codec.INT.fieldOf("xp").forGetter(Raw::xp),
		Codec.STRING.listOf().optionalFieldOf("unlocked", List.of()).forGetter(Raw::unlocked)
	).apply(i, Raw::new));

	public static final Codec<PlayerData> CODEC = RAW.flatXmap(PlayerDataCodec::fromRaw, d -> DataResult.success(toRaw(d)));

	private static Raw toRaw(PlayerData d) {
		return new Raw(d.playerClass().map(PlayerClass::name), d.progress().level(), d.progress().xp(),
			new ArrayList<>(d.progress().unlocked()));
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
		return DataResult.success(d);
	}
}
