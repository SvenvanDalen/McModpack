package com.drakenrijken.core;

/** Tijdperk-poorten: tijdperk 2 en 3 gaan open zodra de bijbehorende quest klaar is. */
public final class EraGate {
	private final String era2Quest;
	private final String era3Quest;

	public EraGate(String era2Quest, String era3Quest) {
		if (era2Quest == null || era3Quest == null || era2Quest.isBlank() || era3Quest.isBlank()) {
			throw new IllegalArgumentException("quest-id's mogen niet leeg zijn");
		}
		this.era2Quest = era2Quest;
		this.era3Quest = era3Quest;
	}

	/** Hoogste ontgrendelde tijdperk (1, 2 of 3). Tijdperk 3 vraagt ook dat tijdperk 2 open is. */
	public int currentEra(QuestProgress quests) {
		if (!quests.isCompleted(era2Quest)) return 1;
		return quests.isCompleted(era3Quest) ? 3 : 2;
	}

	public boolean isUnlocked(QuestProgress quests, int era) {
		if (era < 1 || era > 3) throw new IllegalArgumentException("tijdperk buiten 1..3: " + era);
		return era <= currentEra(quests);
	}
}
