package com.drakenrijken.config;

import com.drakenrijken.DrakenRijken;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Leest of maakt config/drakenrijken.json. */
public final class ConfigFile {
	private ConfigFile() {}

	public static ModConfig load(Path configDir) {
		Path file = configDir.resolve("drakenrijken.json");
		try {
			if (!Files.exists(file)) {
				Files.createDirectories(configDir);
				Files.writeString(file, "{\n  \"particles\": true,\n  \"maxActiveMachines\": 64\n}\n", StandardCharsets.UTF_8);
				return ModConfig.defaults();
			}
			return ModConfig.parse(Files.readString(file, StandardCharsets.UTF_8));
		} catch (IOException e) {
			DrakenRijken.LOGGER.warn("Config niet leesbaar, standaardwaarden gebruikt: {}", e.getMessage());
			return ModConfig.defaults();
		}
	}
}
