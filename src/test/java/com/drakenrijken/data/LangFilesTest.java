package com.drakenrijken.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Nederlandse en Engelse teksten moeten gelijke sleutels hebben en alle vaardigheden en klassen dekken. */
class LangFilesTest {
	private static JsonObject lang(String name) throws IOException {
		try (InputStream in = LangFilesTest.class.getResourceAsStream("/assets/drakenrijken/lang/" + name);
				Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(r).getAsJsonObject();
		}
	}

	@Test
	void gelijkeSleutelsEnGeenLegeTeksten() throws IOException {
		JsonObject nl = lang("nl_nl.json");
		JsonObject en = lang("en_us.json");
		assertEquals(nl.keySet(), en.keySet());
		for (String k : nl.keySet()) {
			assertTrue(!nl.get(k).getAsString().isBlank() && !en.get(k).getAsString().isBlank(), k);
		}
	}

	@Test
	void elkeVaardigheidEnKlasseHeeftEenNaam() throws IOException, URISyntaxException {
		Set<String> keys = lang("nl_nl.json").keySet();
		Path dir = Paths.get(LangFilesTest.class.getResource("/data/drakenrijken/skills").toURI());
		Set<String> missing = new HashSet<>();
		try (Stream<Path> files = Files.list(dir)) {
			for (Path p : files.filter(f -> f.toString().endsWith(".json")).toList()) {
				String id = JsonParser.parseString(Files.readString(p)).getAsJsonObject().get("id").getAsString();
				if (!keys.contains("skill.drakenrijken." + id)) missing.add(id);
			}
		}
		assertTrue(missing.isEmpty(), "zonder naam: " + missing);
		for (String c : new String[] {"ridder", "magier", "schurk", "samoerai", "onmyoji"}) assertTrue(keys.contains("class.drakenrijken." + c));
	}
}
