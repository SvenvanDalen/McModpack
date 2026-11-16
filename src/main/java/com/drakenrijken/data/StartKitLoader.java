package com.drakenrijken.data;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.StartKit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** Leest startsets uit JSON; een kapot bestand of dubbele klasse wordt overgeslagen met een waarschuwing. */
public final class StartKitLoader {
	private StartKitLoader() {}

	public static List<StartKit> load(Stream<String> jsonFiles, Consumer<String> warn) {
		List<StartKit> result = new ArrayList<>();
		Set<PlayerClass> seen = EnumSet.noneOf(PlayerClass.class);
		int[] index = {0};
		jsonFiles.forEach(text -> {
			int n = index[0]++;
			try {
				StartKit k = parse(text);
				if (!seen.add(k.playerClass())) {
					warn.accept("Startset-bestand " + n + " overgeslagen: dubbele klasse " + k.playerClass());
					return;
				}
				result.add(k);
			} catch (RuntimeException e) {
				warn.accept("Startset-bestand " + n + " overgeslagen: " + e.getMessage());
			}
		});
		return result;
	}

	private static StartKit parse(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject()) throw new IllegalArgumentException("geen JSON-object");
		JsonObject o = root.getAsJsonObject();
		if (!o.has("class") || !o.has("items")) throw new IllegalArgumentException("veld ontbreekt (class of items)");
		PlayerClass pc = PlayerClass.parse(o.get("class").getAsString())
			.orElseThrow(() -> new IllegalArgumentException("onbekende klasse: " + o.get("class").getAsString()));
		Map<String, Integer> items = new LinkedHashMap<>();
		o.getAsJsonObject("items").entrySet().forEach(e -> items.put(e.getKey(), e.getValue().getAsInt()));
		return new StartKit(pc, items);
	}
}
