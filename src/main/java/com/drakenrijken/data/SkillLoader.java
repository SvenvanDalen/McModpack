package com.drakenrijken.data;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.Skill;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** Leest vaardigheden uit JSON; een ongeldig bestand wordt overgeslagen met een waarschuwing, de rest laadt. */
public final class SkillLoader {
	private SkillLoader() {}

	public static List<Skill> load(Stream<String> jsonFiles, Consumer<String> warn) {
		List<Skill> parsed = new ArrayList<>();
		Set<String> ids = new HashSet<>();
		int[] index = {0};
		jsonFiles.forEach(text -> {
			int n = index[0]++;
			try {
				Skill s = parse(text);
				if (!ids.add(s.id())) {
					warn.accept("Vaardigheid-bestand " + n + " overgeslagen: dubbele id " + s.id());
					return;
				}
				parsed.add(s);
			} catch (RuntimeException e) {
				warn.accept("Vaardigheid-bestand " + n + " overgeslagen: " + e.getMessage());
			}
		});
		boolean changed = true;
		while (changed) {
			changed = false;
			Set<String> known = new HashSet<>();
			parsed.forEach(s -> known.add(s.id()));
			for (var it = parsed.iterator(); it.hasNext();) {
				Skill s = it.next();
				if (!known.containsAll(s.requires())) {
					warn.accept("Vaardigheid " + s.id() + " overgeslagen: onbekende voorwaarde");
					it.remove();
					changed = true;
				}
			}
		}
		return parsed;
	}

	private static Skill parse(String text) {
		JsonElement root = JsonParser.parseString(text);
		if (!root.isJsonObject()) throw new IllegalArgumentException("geen JSON-object");
		JsonObject o = root.getAsJsonObject();
		List<String> requires = new ArrayList<>();
		if (o.has("requires")) o.getAsJsonArray("requires").forEach(e -> requires.add(e.getAsString()));
		if (!o.has("id") || !o.has("class")) throw new IllegalArgumentException("veld ontbreekt (id of class)");
		PlayerClass pc;
		try {
			pc = PlayerClass.valueOf(o.get("class").getAsString());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("onbekende klasse " + o.get("class").getAsString());
		}
		return new Skill(o.get("id").getAsString(), pc, requires);
	}
}
