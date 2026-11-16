package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Telt events en voltooit quests; ketens (requires) gaan in volgorde. */
public final class QuestEngine {
	private final List<QuestDef> quests;

	public QuestEngine(List<QuestDef> quests) {
		Set<String> ids = new HashSet<>();
		for (QuestDef q : quests) {
			if (!ids.add(q.id())) throw new IllegalArgumentException("dubbele quest: " + q.id());
		}
		for (QuestDef q : quests) {
			for (String r : q.requires()) {
				if (!ids.contains(r)) throw new IllegalArgumentException("quest " + q.id() + " vereist onbekende quest " + r);
			}
		}
		this.quests = List.copyOf(quests);
	}

	public List<QuestDef> quests() { return quests; }

	/** Verwerkt een event; geeft de ids van de quests die hierdoor voltooid zijn. */
	public List<String> onEvent(QuestEvent e, PlayerData d) {
		QuestProgress qp = d.quests();
		List<String> done = new ArrayList<>();
		for (QuestDef q : quests) {
			if (qp.isCompleted(q.id()) || !qp.completed().containsAll(q.requires()) || !q.matches(e)) continue;
			int n = qp.progress(q.id()) + 1;
			qp.counts().put(q.id(), n);
			if (n >= q.count()) {
				qp.completed().add(q.id());
				done.add(q.id());
			}
		}
		return done;
	}
}
