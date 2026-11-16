package com.drakenrijken.data;

import com.drakenrijken.DrakenRijken;
import com.drakenrijken.core.MachineRecipeBook;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;

/** Laadt de machinerecepten uit de mod-bestanden en houdt het receptenboek vast (DR-411). */
public final class ModRecipes {
	private static MachineRecipeBook book = new MachineRecipeBook(List.of());

	private ModRecipes() {}

	public static MachineRecipeBook book() { return book; }

	public static void init() {
		Path dir = FabricLoader.getInstance().getModContainer(DrakenRijken.MOD_ID)
			.flatMap(c -> c.findPath("data/drakenrijken/recipes")).orElse(null);
		if (dir == null) return;
		try (Stream<Path> files = Files.list(dir)) {
			book = new MachineRecipeBook(MachineRecipeLoader.load(files.filter(f -> f.toString().endsWith(".json")).sorted().map(f -> {
				try {
					return Files.readString(f);
				} catch (IOException e) {
					return "";
				}
			}), DrakenRijken.LOGGER::warn));
		} catch (IOException | IllegalArgumentException e) {
			DrakenRijken.LOGGER.warn("Machinerecepten niet te laden: {}", e.getMessage());
		}
	}
}
