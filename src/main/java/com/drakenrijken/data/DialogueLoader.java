package com.drakenrijken.data;

import com.drakenrijken.core.Dialogue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** Leest gesprekken uit JSON; een ongeldig bestand wordt overgeslagen met een waarschuwing. */
public final class DialogueLoader {
	private DialogueLoader() {}

	public static List<Dialogue> load(Stream<String> jsonFiles, Consumer<String> warn) {
		List<Dialogue> result = new ArrayList<>();
		int[] index = {0};
		jsonFiles.forEach(text -> {
			int n = index[0]++;
			try {
				result.add(parse(text));
			} catch (RuntimeException e) {
				warn.accept("Gesprek-bestand " + n + " overgeslagen: " + e.getMessage());
			}
		});
		return result;
	}

	private static Dialogue parse(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject()) throw new IllegalArgumentException("geen JSON-object");
		JsonObject o = root.getAsJsonObject();
		Map<String, Dialogue.Node> nodes = new LinkedHashMap<>();
		JsonObject jn = o.getAsJsonObject("nodes");
		if (jn == null) throw new IllegalArgumentException("veld ontbreekt: nodes");
		for (Map.Entry<String, JsonElement> e : jn.entrySet()) {
			JsonObject n = e.getValue().getAsJsonObject();
			List<Dialogue.Choice> choices = new ArrayList<>();
			if (n.has("choices")) {
				n.getAsJsonArray("choices").forEach(c -> {
					JsonObject co = c.getAsJsonObject();
					choices.add(new Dialogue.Choice(required(co, "text"),
						co.has("next") && !co.get("next").isJsonNull() ? co.get("next").getAsString() : null));
				});
			}
			nodes.put(e.getKey(), new Dialogue.Node(required(n, "text"), choices));
		}
		return new Dialogue(required(o, "id"), required(o, "start"), nodes);
	}

	private static String required(JsonObject o, String key) {
		if (!o.has(key)) throw new IllegalArgumentException("veld ontbreekt: " + key);
		return o.get(key).getAsString();
	}
}
