package com.drakenrijken.core;

/**
 * Startmodus (DR-1111): hoe een nieuwe wereld begint. Eerste stap: alleen de keuze of er structuren gegenereerd worden.
 * Later uit te breiden met bijvoorbeeld een eigen startpakket of eigen quests voor de lege kavel (apart ontwerp nodig).
 */
public enum StartMode {
	NORMAL("normaal", true),
	EMPTY_PLOT("lege-kavel", false);

	private final String key;
	private final boolean structures;

	StartMode(String key, boolean structures) {
		this.key = key;
		this.structures = structures;
	}

	/** Naam zoals in drakenrijken.json. */
	public String key() { return key; }

	/** Of dorpen, steden, burchten en drakengebieden gegenereerd mogen worden. */
	public boolean generatesStructures() { return structures; }

	/** Onbekende of ontbrekende waarde valt terug op de normale start. */
	public static StartMode parse(String key) {
		for (StartMode m : values()) if (m.key.equals(key)) return m;
		return NORMAL;
	}
}
