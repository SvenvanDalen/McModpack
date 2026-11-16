package com.drakenrijken.data;

import com.drakenrijken.core.LootTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/** Leest een buittabel uit JSON: een object met "entries". Een kapotte regel maakt de hele tabel ongeldig. */
public final class LootTableLoader {
	private LootTableLoader() {}

	public static LootTable load(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject() || !root.getAsJsonObject().has("entries")) {
			throw new IllegalArgumentException("geen buittabel: 'entries' ontbreekt");
		}
		List<LootTable.Entry> list = new ArrayList<>();
		for (JsonElement el : root.getAsJsonObject().getAsJsonArray("entries")) {
			JsonObject o = el.getAsJsonObject();
			list.add(new LootTable.Entry(
				o.get("item").getAsString(),
				o.has("count") ? o.get("count").getAsInt() : 1,
				o.has("weight") ? o.get("weight").getAsInt() : 1,
				o.has("era") ? o.get("era").getAsInt() : 1));
		}
		return new LootTable(list);
	}

	/** Zoals load, maar een kapotte tabel geeft een waarschuwing en een lege uitkomst in plaats van een fout. */
	public static Optional<LootTable> loadSafe(String text, Consumer<String> warn) {
		try {
			return Optional.of(load(text));
		} catch (RuntimeException e) {
			warn.accept("buittabel overgeslagen: " + e.getMessage());
			return Optional.empty();
		}
	}
}
