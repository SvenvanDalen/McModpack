package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Bewaakt dat core/ geen Minecraft- of Fabric-code importeert. */
class CoreIsolationTest {
	private static final Path CORE = Path.of("src/main/java/com/drakenrijken/core");

	@Test
	void coreMapBestaat() {
		assertTrue(Files.isDirectory(CORE), "core-map niet gevonden vanuit " + Path.of("").toAbsolutePath());
	}

	@Test
	void coreHeeftGeenMinecraftImports() throws IOException {
		List<String> fouten;
		try (Stream<Path> files = Files.walk(CORE)) {
			fouten = files.filter(p -> p.toString().endsWith(".java")).flatMap(p -> {
				try {
					return Files.readAllLines(p).stream()
						.filter(l -> l.matches("\\s*(import|package)\\s+(static\\s+)?(net\\.minecraft|net\\.fabricmc|com\\.mojang)\\..*"))
						.map(l -> p + ": " + l.trim());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}).toList();
		}
		assertEquals(List.of(), fouten);
	}
}
