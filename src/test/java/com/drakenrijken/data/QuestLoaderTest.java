package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drakenrijken.core.QuestDef;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class QuestLoaderTest {
	private static final String HOUT = "{\"id\":\"hout\",\"event\":\"break_block\",\"target\":\"minecraft:oak_log\",\"count\":5}";
	private static final String SMEDERIJ = "{\"id\":\"smederij\",\"requires\":[\"hout\"],\"event\":\"craft\",\"target\":\"*\",\"count\":1}";

	private final List<String> warnings = new ArrayList<>();

	private List<QuestDef> load(String... files) {
		return QuestLoader.load(Stream.of(files), warnings::add);
	}

	@Test
	void laadtGeldigeQuests() {
		List<QuestDef> q = load(HOUT, SMEDERIJ);
		assertEquals(2, q.size());
		assertEquals(List.of("hout"), q.get(1).requires());
		assertTrue(warnings.isEmpty());
	}

	@Test
	void kapotBestandWordtOvergeslagenRestLaadt() {
		List<QuestDef> q = load(HOUT, "{kapot", SMEDERIJ);
		assertEquals(2, q.size());
		assertEquals(1, warnings.size());
	}

	@Test
	void ontbrekendeVeldenEnOnzinWordenOvergeslagen() {
		List<QuestDef> q = load("{\"id\":\"x\"}", "[]", "", "{\"id\":\"y\",\"event\":\"kill\",\"target\":\"*\",\"count\":0}", HOUT);
		assertEquals(1, q.size());
		assertEquals(4, warnings.size());
	}

	@Test
	void dubbeleIdEnOnbekendeVoorwaardeWordenOvergeslagen() {
		List<QuestDef> q = load(HOUT, HOUT, SMEDERIJ.replace("hout", "weg"));
		assertEquals(1, q.size());
		assertEquals(2, warnings.size());
	}

	@Test
	void voorwaardeKetenVanOvergeslagenQuestVervaltOok() {
		List<QuestDef> q = load("{kapot", SMEDERIJ);
		assertTrue(q.isEmpty());
	}
}
