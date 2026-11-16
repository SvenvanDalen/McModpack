package com.drakenrijken.core;

import java.util.Optional;

/** Alle opgeslagen spelerdata: gekozen klasse (nog niet gekozen = leeg) en voortgang. */
public final class PlayerData {
	private PlayerClass playerClass;
	private final PlayerProgress progress = new PlayerProgress();
	private final QuestProgress quests = new QuestProgress();

	private PlayerData() {}

	public static PlayerData empty() {
		return new PlayerData();
	}

	public Optional<PlayerClass> playerClass() { return Optional.ofNullable(playerClass); }

	public void setPlayerClass(PlayerClass playerClass) { this.playerClass = playerClass; }

	public PlayerProgress progress() { return progress; }

	public QuestProgress quests() { return quests; }
}
