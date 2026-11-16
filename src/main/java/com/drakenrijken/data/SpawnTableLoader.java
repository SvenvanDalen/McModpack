package com.drakenrijken.data;

import com.drakenrijken.core.SpawnTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** Leest spawnlijsten uit JSON: een object met "area" en "entries". Kapotte of dubbele gebieden worden overgeslagen. */
public final class SpawnTableLoader {
	private SpawnTableLoader() {}

	public static SpawnTable load(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject() || !root.getAsJsonObject().has("area") || !root.getAsJsonObject().has("entries")) {
			throw new IllegalArgumentException("geen spawnlijst: 'area' of 'entries' ontbreekt");
		}
		JsonObject obj = root.getAsJsonObject();
		List<SpawnTable.Entry> list = new ArrayList<>();
		for (JsonElement el : obj.getAsJsonArray("entries")) {
			JsonObject o = el.getAsJsonObject();
			list.add(new SpawnTable.Entry(
				o.get("mob").getAsString(),
				o.has("weight") ? o.get("weight").getAsInt() : 1,
				o.has("era") ? o.get("era").getAsInt() : 1));
		}
		return new SpawnTable(obj.get("area").getAsString(), list);
	}

	/** Laadt meerdere bestanden; een kapot of dubbel bestand geeft een waarschuwing en de rest laadt gewoon. */
	public static Map<String, SpawnTable> loadAll(Stream<String> texts, Consumer<String> warn) {
		Map<String, SpawnTable> out = new LinkedHashMap<>();
		texts.forEach(t -> {
			try {
				SpawnTable s = load(t);
				if (out.putIfAbsent(s.area(), s) != null) warn.accept("dubbel gebied overgeslagen: " + s.area());
			} catch (RuntimeException e) {
				warn.accept("kapotte spawnlijst overgeslagen: " + e.getMessage());
			}
		});
		return out;
	}
}
