package com.drakenrijken.core;

/** Iets wat de speler deed: type (bijv. "kill") en doel (bijv. "minecraft:zombie"). */
public record QuestEvent(String type, String target) {}
