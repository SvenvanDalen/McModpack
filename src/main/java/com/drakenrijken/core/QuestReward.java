package com.drakenrijken.core;

import java.util.List;

/** Beloning bij het voltooien van een quest: xp en items (item-id's, bijv. "minecraft:bread"). */
public record QuestReward(int xp, List<String> items) {
	public static final QuestReward NONE = new QuestReward(0, List.of());

	public QuestReward {
		if (xp < 0) throw new IllegalArgumentException("beloning-xp is negatief: " + xp);
		items = List.copyOf(items);
	}
}
