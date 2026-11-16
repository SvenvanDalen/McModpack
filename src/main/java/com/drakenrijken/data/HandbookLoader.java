package com.drakenrijken.data;

import com.drakenrijken.core.Handbook;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** Leest de handleiding uit JSON; kapotte tekst geeft een waarschuwing en een lege handleiding. */
public final class HandbookLoader {
	private HandbookLoader() {}

	public static Handbook load(String text, Consumer<String> warn) {
		try {
			JsonElement root = JsonParser.parseString(text);
			if (!root.isJsonObject() || !root.getAsJsonObject().has("chapters")) throw new IllegalArgumentException("veld chapters ontbreekt");
			List<Handbook.Chapter> result = new ArrayList<>();
			for (JsonElement e : root.getAsJsonObject().getAsJsonArray("chapters")) {
				JsonObject o = e.getAsJsonObject();
				List<String> lines = new ArrayList<>();
				for (JsonElement l : o.getAsJsonArray("lines")) lines.add(l.getAsString());
				result.add(new Handbook.Chapter(o.get("id").getAsString(), o.get("title").getAsString(), lines));
			}
			return new Handbook(result);
		} catch (RuntimeException e) {
			warn.accept("Handleiding overgeslagen: " + e.getMessage());
			return new Handbook(List.of());
		}
	}
}
