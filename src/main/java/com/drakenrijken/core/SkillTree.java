package com.drakenrijken.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SkillTree {
	public enum Result { OK, NO_POINTS, LOCKED, UNKNOWN, ALREADY_UNLOCKED, WRONG_CLASS }

	private final Map<String, Skill> skills = new HashMap<>();

	public SkillTree(List<Skill> list) {
		for (Skill s : list) {
			if (skills.put(s.id(), s) != null) {
				throw new IllegalArgumentException("dubbele vaardigheid: " + s.id());
			}
		}
	}

	/** Een punt per level boven level 1, plus bonuspunten uit quests. */
	public static int pointsEarned(PlayerProgress p) {
		return p.level() - 1 + p.bonusPoints();
	}
	/** Alle vaardigheden van een klasse, op id gesorteerd. */
	public List<Skill> skillsFor(PlayerClass c) {
		return skills.values().stream().filter(s -> s.playerClass() == c).sorted(java.util.Comparator.comparing(Skill::id)).toList();
	}


	public int pointsLeft(PlayerProgress p) {
		return pointsEarned(p) - p.unlocked().size();
	}

	public Result unlock(PlayerProgress p, PlayerClass playerClass, String id) {
		Skill skill = skills.get(id);
		if (skill == null) return Result.UNKNOWN;
		if (skill.playerClass() != playerClass) return Result.WRONG_CLASS;
		if (p.unlocked().contains(id)) return Result.ALREADY_UNLOCKED;
		if (!p.unlocked().containsAll(skill.requires())) return Result.LOCKED;
		if (pointsLeft(p) < 1) return Result.NO_POINTS;
		p.unlocked().add(id);
		return Result.OK;
	}

	public static final int RESPEC_COST_PER_SKILL = 10;

	/** Prijs om alle vaardigheden te herverdelen (eerste inschatting): 10 munten per ontgrendelde vaardigheid. */
	public static int respecCost(PlayerProgress p) {
		return p.unlocked().size() * RESPEC_COST_PER_SKILL;
	}

	/**
	 * Herverdelen: pas als de speler genoeg munten heeft worden alle vaardigheden teruggezet
	 * (de punten komen vrij). Geeft de betaalde prijs, of -1 als er te weinig munten zijn.
	 */
	public static int respec(PlayerProgress p, int coins) {
		int cost = respecCost(p);
		if (coins < cost) return -1;
		p.unlocked().clear();
		return cost;
	}
}
