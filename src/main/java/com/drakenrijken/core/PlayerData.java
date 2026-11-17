package com.drakenrijken.core;

import java.util.Optional;

/** Alle opgeslagen spelerdata: gekozen klasse (nog niet gekozen = leeg) en voortgang. */
public final class PlayerData {
	private PlayerClass playerClass;
	private final PlayerProgress progress = new PlayerProgress();
	private final QuestProgress quests = new QuestProgress();
	private int realmPower;
	private long lastTaxDay;
	private final java.util.Set<String> discovered = new java.util.LinkedHashSet<>();

	private PlayerData() {}

	public static PlayerData empty() {
		return new PlayerData();
	}

	public Optional<PlayerClass> playerClass() { return Optional.ofNullable(playerClass); }

	public void setPlayerClass(PlayerClass playerClass) { this.playerClass = playerClass; }

	public PlayerProgress progress() { return progress; }

	public QuestProgress quests() { return quests; }

	/** Ontdekte koninkrijken als "x,z" (midden van het koninkrijk), in volgorde van ontdekking. */
	public java.util.Set<String> discovered() { return discovered; }

	public int realmPower() { return realmPower; }

	/** Speldag waarop de belasting (RealmTax) voor het laatst is opgehaald. */
	public long lastTaxDay() { return lastTaxDay; }

	public void setLastTaxDay(long day) { this.lastTaxDay = day; }

	/** Totale macht van het rijk; het niveau volgt hieruit via RealmState. */
	public void setRealmPower(int power) {
		if (power < 0) throw new IllegalArgumentException("macht is negatief: " + power);
		this.realmPower = power;
	}
}
