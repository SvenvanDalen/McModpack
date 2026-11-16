package com.drakenrijken.data;

import com.drakenrijken.DrakenRijken;
import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.StartKit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/** Laadt de startsets uit de mod-bestanden en geeft ze aan een speler die een klasse kiest (DR-111). */
public final class ModKits {
	private static List<StartKit> kits = List.of();

	private ModKits() {}

	public static void init() {
		Path dir = FabricLoader.getInstance().getModContainer(DrakenRijken.MOD_ID)
			.flatMap(c -> c.findPath("data/drakenrijken/kits")).orElse(null);
		if (dir == null) return;
		try (Stream<Path> files = Files.list(dir)) {
			kits = StartKitLoader.load(files.filter(f -> f.toString().endsWith(".json")).sorted().map(f -> {
				try {
					return Files.readString(f);
				} catch (IOException e) {
					return "";
				}
			}), DrakenRijken.LOGGER::warn);
		} catch (IOException e) {
			DrakenRijken.LOGGER.warn("Startsets niet te laden: {}", e.getMessage());
		}
	}

	/** Geeft de startset van de klasse; onbekende items worden overgeslagen met een waarschuwing. */
	public static void give(ServerPlayer player, PlayerClass klasse) {
		for (var e : StartKit.forClass(kits, klasse).items().entrySet()) {
			Identifier id = Identifier.tryParse(e.getKey());
			var item = id == null ? java.util.Optional.<net.minecraft.world.item.Item>empty() : BuiltInRegistries.ITEM.getOptional(id);
			if (item.isEmpty()) {
				DrakenRijken.LOGGER.warn("Startset {} bevat onbekend item {}", klasse, e.getKey());
				continue;
			}
			player.getInventory().add(new ItemStack(item.get(), e.getValue()));
		}
	}
}
