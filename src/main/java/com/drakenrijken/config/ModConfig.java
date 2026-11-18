package com.drakenrijken.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** Instellingen uit drakenrijken.json; kapotte of ontbrekende waarden vallen terug op de standaard. */
public record ModConfig(boolean particles, int maxActiveMachines, boolean generateStructures, boolean highContrast, double textScale, com.drakenrijken.core.StartMode startMode) {
	public static final int DEFAULT_MAX_MACHINES = 64;

	public ModConfig(boolean particles, int maxActiveMachines, boolean generateStructures) {
		this(particles, maxActiveMachines, generateStructures, false, 1.0, com.drakenrijken.core.StartMode.NORMAL);
	}

	public ModConfig(boolean particles, int maxActiveMachines, boolean generateStructures, boolean highContrast, double textScale) {
		this(particles, maxActiveMachines, generateStructures, highContrast, textScale, com.drakenrijken.core.StartMode.NORMAL);
	}

	/** Structuren worden alleen gebouwd als de instelling aan staat en de startmodus het toestaat (niet bij de lege kavel). */
	public boolean buildsStructures() {
		return generateStructures && startMode.generatesStructures();
	}

	public static ModConfig defaults() {
		return new ModConfig(true, DEFAULT_MAX_MACHINES, false);
	}

	public static ModConfig parse(String json) {
		try {
			JsonObject o = JsonParser.parseString(json).getAsJsonObject();
			return new ModConfig(bool(o, "particles", true), positiveInt(o, "maxActiveMachines", DEFAULT_MAX_MACHINES), bool(o, "generateStructures", false), bool(o, "highContrast", false), scale(o), startMode(o));
		} catch (RuntimeException e) {
			return defaults();
		}
	}

	private static com.drakenrijken.core.StartMode startMode(JsonObject o) {
		try {
			return com.drakenrijken.core.StartMode.parse(o.get("startMode").getAsString());
		} catch (RuntimeException e) {
			return com.drakenrijken.core.StartMode.NORMAL;
		}
	}

	/** Tekstgrootte voor de eigen schermen: tussen 1,0 en 2,0, anders 1,0. */
	private static double scale(JsonObject o) {
		try {
			double s = o.get("textScale").getAsDouble();
			return s >= 1.0 && s <= 2.0 ? s : 1.0;
		} catch (RuntimeException e) {
			return 1.0;
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
