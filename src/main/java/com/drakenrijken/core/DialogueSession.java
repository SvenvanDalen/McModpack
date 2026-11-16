package com.drakenrijken.core;

/** Loopt door een gesprek: toont het huidige knooppunt en volgt keuzes. */
public final class DialogueSession {
	private final Dialogue dialogue;
	private String current;

	public DialogueSession(Dialogue dialogue) {
		this.dialogue = dialogue;
		this.current = dialogue.start();
	}

	public boolean finished() { return current == null; }

	/** Het huidige knooppunt; alleen aanroepen als het gesprek niet klaar is. */
	public Dialogue.Node node() {
		if (finished()) throw new IllegalStateException("gesprek is afgelopen");
		return dialogue.nodes().get(current);
	}

	/** Kiest een antwoord (0-gebaseerd). */
	public void choose(int index) {
		Dialogue.Node n = node();
		if (index < 0 || index >= n.choices().size()) {
			throw new IllegalArgumentException("ongeldige keuze: " + index);
		}
		current = n.choices().get(index).next();
	}

	/** Voor een knooppunt zonder keuzes: sluit het gesprek af. */
	public void advance() {
		if (!node().choices().isEmpty()) {
			throw new IllegalStateException("kies een antwoord");
		}
		current = null;
	}
}
