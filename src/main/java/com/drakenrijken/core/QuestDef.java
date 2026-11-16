package com.drakenrijken.core;

import java.util.List;

/** Een quest: voltooid na count passende events, pas actief als alle requires voltooid zijn. Doel "*" past op alles. */
public record QuestDef(String id, List<String> requires, String eventType, String target, int count) {
	public QuestDef {
		if (id == null || id.isBlank()) throw new IllegalArgumentException("quest zonder id");
		if (count < 1) throw new IllegalArgumentException("count moet minstens 1 zijn: " + id);
		requires = List.copyOf(requires);
	}

	boolean matches(QuestEvent e) {
		return eventType.equals(e.type()) && (target.equals("*") || target.equals(e.target()));
	}
}
