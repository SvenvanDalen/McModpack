package com.drakenrijken.core;

import java.util.List;

/** Kiest de ene regel voor de HUD-tracker: de eerste actieve quest, of leeg als er geen is. */
public final class QuestTracker {
	private QuestTracker() {}

	public static String line(List<QuestDef> quests, QuestProgress progress) {
		for (QuestDef q : quests) {
			if (!progress.isCompleted(q.id()) && progress.completed().containsAll(q.requires())) {
				return q.id() + ": " + progress.progress(q.id()) + "/" + q.count();
			}
		}
		return "";
	}
}
