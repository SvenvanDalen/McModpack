package com.drakenrijken.data;

import com.drakenrijken.core.MachineRecipe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** Leest machinerecepten uit JSON; een ongeldig bestand wordt overgeslagen met een waarschuwing, de rest laadt. */
public final class MachineRecipeLoader {
	private MachineRecipeLoader() {}

	public static List<MachineRecipe> load(Stream<String> jsonFiles, Consumer<String> warn) {
		List<MachineRecipe> out = new ArrayList<>();
		Set<String> ids = new HashSet<>();
		int[] index = {0};
		jsonFiles.forEach(text -> {
			int n = index[0]++;
			try {
				MachineRecipe r = parse(text);
				if (!ids.add(r.id())) {
					warn.accept("Recept-bestand " + n + " overgeslagen: dubbele id " + r.id());
					return;
				}
				out.add(r);
			} catch (RuntimeException e) {
				warn.accept("Recept-bestand " + n + " overgeslagen: " + e.getMessage());
			}
		});
		return out;
	}

	private static MachineRecipe parse(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject()) throw new IllegalArgumentException("geen JSON-object");
		JsonObject o = root.getAsJsonObject();
		if (!o.has("inputs") || !o.get("inputs").isJsonObject()) throw new IllegalArgumentException("veld ontbreekt: inputs");
		Map<String, Integer> inputs = new LinkedHashMap<>();
		o.getAsJsonObject("inputs").entrySet().forEach(e -> inputs.put(e.getKey(), e.getValue().getAsInt()));
		return new MachineRecipe(required(o, "id"), required(o, "machine"), inputs, required(o, "output"),
			o.has("count") ? o.get("count").getAsInt() : 1, o.has("ticks") ? o.get("ticks").getAsInt() : 200,
			o.has("power") ? o.get("power").getAsInt() : 0);
	}

	private static String required(JsonObject o, String key) {
		if (!o.has(key)) throw new IllegalArgumentException("veld ontbreekt: " + key);
		return o.get(key).getAsString();
	}
}
