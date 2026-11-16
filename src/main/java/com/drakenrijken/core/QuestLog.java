package com.drakenrijken.core;

import java.util.ArrayList;
import java.util.List;

/** Maakt de regels voor het questlog: actieve quests met voortgang, daarna voltooide. Quests met onvoltooide voorwaarden blijven verborgen. */
public final class QuestLog {
	private QuestLog() {}

	public static List<String> lines(List<QuestDef> quests, QuestProgress progress) {
		List<String> active = new ArrayList<>();
		List<String> done = new ArrayList<>();
		for (QuestDef q : quests) {
			if (progress.isCompleted(q.id())) {
				done.add("[klaar] " + q.id());
			} else if (progress.completed().containsAll(q.requires())) {
				active.add("[bezig] " + q.id() + ": " + q.eventType() + " " + q.target()
					+ " (" + progress.progress(q.id()) + "/" + q.count() + ")");
			}
		}
		active.addAll(done);
		return active;
	}
}
