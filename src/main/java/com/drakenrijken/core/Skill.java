package com.drakenrijken.core;

import java.util.List;

/** Een vaardigheid; kost altijd 1 punt. requires zijn ids die eerst ontgrendeld moeten zijn. */
public record Skill(String id, PlayerClass playerClass, List<String> requires) {
	public Skill {
		requires = List.copyOf(requires);
	}
}
