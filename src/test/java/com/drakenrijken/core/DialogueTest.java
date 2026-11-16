package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.data.DialogueLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DialogueTest {
	private static final String JSON = "{\"id\":\"smid\",\"start\":\"a\",\"nodes\":{"
		+ "\"a\":{\"text\":\"Hallo\",\"choices\":[{\"text\":\"Werk?\",\"next\":\"b\"},{\"text\":\"Dag\"}]},"
		+ "\"b\":{\"text\":\"Breng hout\"}}}";

	private static DialogueSession session() {
		return new DialogueSession(DialogueLoader.load(Stream.of(JSON), m -> {}).get(0));
	}

	@Test
	void keuzeLeidtNaarVolgendKnooppuntEnAfsluiten() {
		DialogueSession s = session();
		assertEquals("Hallo", s.node().text());
		s.choose(0);
		assertEquals("Breng hout", s.node().text());
		s.advance();
		assertTrue(s.finished());
	}

	@Test
	void keuzeZonderDoelEindigtHetGesprek() {
		DialogueSession s = session();
		s.choose(1);
		assertTrue(s.finished());
		assertThrows(IllegalStateException.class, s::node);
	}

	@Test
	void ongeldigeKeuzeEnKapotteDataWordenAfgewezen() {
		DialogueSession s = session();
		assertThrows(IllegalArgumentException.class, () -> s.choose(5));
		assertThrows(IllegalStateException.class, s::advance);
		List<String> warns = new ArrayList<>();
		String kapot = "{\"id\":\"x\",\"start\":\"a\",\"nodes\":{\"a\":{\"text\":\"t\",\"choices\":[{\"text\":\"c\",\"next\":\"weg\"}]}}}";
		assertTrue(DialogueLoader.load(Stream.of(kapot, "geen json {"), warns::add).isEmpty());
		assertEquals(2, warns.size());
	}
}
