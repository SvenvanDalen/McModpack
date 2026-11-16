package com.drakenrijken.data;

import com.drakenrijken.core.QuestDef;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** Leest quest-JSON; een ongeldig bestand wordt overgeslagen met een waarschuwing, de rest laadt. */
public final class QuestLoader {
	private QuestLoader() {}

	public static List<QuestDef> load(Stream<String> jsonFiles, Consumer<String> warn) {
		List<QuestDef> parsed = new ArrayList<>();
		Set<String> ids = new HashSet<>();
		int[] index = {0};
		jsonFiles.forEach(text -> {
			int n = index[0]++;
			try {
				QuestDef q = parse(text);
				if (!ids.add(q.id())) {
					warn.accept("Quest-bestand " + n + " overgeslagen: dubbele id " + q.id());
					return;
				}
				parsed.add(q);
			} catch (RuntimeException e) {
				warn.accept("Quest-bestand " + n + " overgeslagen: " + e.getMessage());
			}
		});
		// Quests die een ontbrekende of weggevallen quest vereisen, vallen ook weg.
		boolean changed = true;
		while (changed) {
			changed = false;
			Set<String> have = new HashSet<>();
			parsed.forEach(q -> have.add(q.id()));
			for (Iterator<QuestDef> it = parsed.iterator(); it.hasNext();) {
				QuestDef q = it.next();
				if (!have.containsAll(q.requires())) {
					warn.accept("Quest " + q.id() + " overgeslagen: vereist een quest die ontbreekt");
					it.remove();
					changed = true;
				}
			}
		}
		return parsed;
	}

	private static QuestDef parse(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject()) throw new IllegalArgumentException("geen JSON-object");
		JsonObject o = root.getAsJsonObject();
		List<String> requires = new ArrayList<>();
		if (o.has("requires")) {
			JsonArray arr = o.getAsJsonArray("requires");
			arr.forEach(e -> requires.add(e.getAsString()));
		}
		return new QuestDef(required(o, "id"), requires, required(o, "event"), required(o, "target"),
			o.has("count") ? o.get("count").getAsInt() : 1);
	}

	private static String required(JsonObject o, String key) {
		if (!o.has(key)) throw new IllegalArgumentException("veld ontbreekt: " + key);
		return o.get(key).getAsString();
	}
}
