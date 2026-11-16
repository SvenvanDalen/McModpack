package com.drakenrijken.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** Instellingen uit drakenrijken.json; kapotte of ontbrekende waarden vallen terug op de standaard. */
public record ModConfig(boolean particles, int maxActiveMachines) {
	public static final int DEFAULT_MAX_MACHINES = 64;

	public static ModConfig defaults() {
		return new ModConfig(true, DEFAULT_MAX_MACHINES);
	}

	public static ModConfig parse(String json) {
		try {
			JsonObject o = JsonParser.parseString(json).getAsJsonObject();
			return new ModConfig(bool(o, "particles", true), positiveInt(o, "maxActiveMachines", DEFAULT_MAX_MACHINES));
		} catch (RuntimeException e) {
			return defaults();
		}
	}

	private static boolean bool(JsonObject o, String key, boolean fallback) {
		JsonElement v = o.get(key);
		try {
			return v != null && v.getAsJsonPrimitive().isBoolean() ? v.getAsBoolean() : fallback;
		} catch (RuntimeException e) {
			return fallback;
		}
	}

	private static int positiveInt(JsonObject o, String key, int fallback) {
		try {
			int n = o.get(key).getAsInt();
			return n > 0 ? n : fallback;
		} catch (RuntimeException e) {
			return fallback;
		}
	}
}
