package com.drakenrijken.core;

import java.util.List;
import java.util.Map;

/** Een gesprek: knooppunten met tekst en keuzes. Een keuze zonder doel (null) beëindigt het gesprek. */
public record Dialogue(String id, String start, Map<String, Node> nodes) {
	public record Choice(String text, String next) {}

	public record Node(String text, List<Choice> choices) {}

	public Dialogue {
		if (!nodes.containsKey(start)) {
			throw new IllegalArgumentException("startknooppunt bestaat niet: " + start);
		}
		for (Node n : nodes.values()) {
			for (Choice c : n.choices()) {
				if (c.next() != null && !nodes.containsKey(c.next())) {
					throw new IllegalArgumentException("keuze verwijst naar onbekend knooppunt: " + c.next());
				}
			}
		}
	}
}
