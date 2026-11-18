package com.drakenrijken.data;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/** Losse voorwerpen van de mod (geen blokken). */
public final class ModTools {
	public static Item MOERSLEUTEL;

	private ModTools() {}

	public static void init() {
		Identifier id = Identifier.fromNamespaceAndPath("drakenrijken", "moersleutel");
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		MOERSLEUTEL = Registry.register(BuiltInRegistries.ITEM, key, new WrenchItem(new Item.Properties().setId(key).stacksTo(1)));
	}
}
