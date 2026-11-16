package com.drakenrijken;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/** De versie in gradle.properties is Semver en staat in de changelog (DR-1003). */
class ChangelogTest {
	@Test
	void versieIsSemverEnStaatInChangelog() throws IOException {
		Matcher m = Pattern.compile("(?m)^version=(.+)$").matcher(Files.readString(Path.of("gradle.properties")));
		assertTrue(m.find(), "geen version in gradle.properties");
		String version = m.group(1).trim();
		String[] delen = version.split("[.]");
		assertTrue(delen.length == 3, "geen Semver: " + version);
		for (String d : delen) assertTrue(!d.isEmpty() && d.chars().allMatch(Character::isDigit), "geen Semver: " + version);
		assertTrue(Files.readString(Path.of("CHANGELOG.md")).contains("## [" + version + "]"), "versie ontbreekt in changelog");
	}
}
