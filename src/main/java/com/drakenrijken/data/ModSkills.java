package com.drakenrijken.data;

import com.drakenrijken.DrakenRijken;
import com.drakenrijken.core.SkillTree;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;

/** Laadt de vaardigheden uit de mod-bestanden en houdt de boom vast. */
public final class ModSkills {
	private static SkillTree tree = new SkillTree(List.of());

	private ModSkills() {}

	public static SkillTree tree() { return tree; }

	public static void init() {
		Path dir = FabricLoader.getInstance().getModContainer(DrakenRijken.MOD_ID)
			.flatMap(c -> c.findPath("data/drakenrijken/skills")).orElse(null);
		if (dir == null) return;
		try (Stream<Path> files = Files.list(dir)) {
			tree = new SkillTree(SkillLoader.load(files.filter(f -> f.toString().endsWith(".json")).sorted().map(f -> {
				try {
					return Files.readString(f);
				} catch (IOException e) {
					return "";
				}
			}), DrakenRijken.LOGGER::warn));
		} catch (IOException e) {
			DrakenRijken.LOGGER.warn("Vaardigheden niet te laden: {}", e.getMessage());
		}
	}
}
