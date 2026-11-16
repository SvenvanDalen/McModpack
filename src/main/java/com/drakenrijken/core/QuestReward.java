package com.drakenrijken.core;

import java.util.List;

/** Beloning bij het voltooien van een quest: xp, vaardigheidspunten en items (item-id's, bijv. "minecraft:bread"). */
public record QuestReward(int xp, int skillPoints, List<String> items) {
	public static final QuestReward NONE = new QuestReward(0, 0, List.of());

	public QuestReward(int xp, List<String> items) {
		this(xp, 0, items);
	}

	public QuestReward {
		if (xp < 0) throw new IllegalArgumentException("beloning-xp is negatief: " + xp);
		if (skillPoints < 0) throw new IllegalArgumentException("beloning-vaardigheidspunten is negatief: " + skillPoints);
		items = List.copyOf(items);
	}
}
