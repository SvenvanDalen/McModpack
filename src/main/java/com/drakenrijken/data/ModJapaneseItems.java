package com.drakenrijken.data;

import com.drakenrijken.core.JapaneseWeapons;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ToolMaterial;

/** Japanse wapens (DR-1205); de getallen komen uit core.JapaneseWeapons. */
public final class ModJapaneseItems {
	public static Item KATANA;
	public static Item NAGINATA;
	public static Item YUMI;
	public static Item SHURIKEN;

	private ModJapaneseItems() {}

	public static void init() {
		JapaneseWeapons.Stats k = JapaneseWeapons.get("katana");
		JapaneseWeapons.Stats n = JapaneseWeapons.get("naginata");
		JapaneseWeapons.Stats y = JapaneseWeapons.get("yumi");
		JapaneseWeapons.Stats s = JapaneseWeapons.get("shuriken");
		// Totale schade = 1 (hand) + ijzerbonus 2 + dit getal; aanvalssnelheid is de afwijking van 4.
		KATANA = register(k.id(), p -> new Item(p.sword(ToolMaterial.IRON, (float) (k.damage() - 3), -2.2F).durability(k.durability())));
		NAGINATA = register(n.id(), p -> new Item(p.sword(ToolMaterial.IRON, (float) (n.damage() - 3), -3.0F).durability(n.durability())));
		YUMI = register(y.id(), p -> new BowItem(p.durability(y.durability())));
		SHURIKEN = register(s.id(), p -> new SnowballItem(p.stacksTo(JapaneseWeapons.SHURIKEN_STACK)));
	}

	private static Item register(String name, Function<Item.Properties, Item> factory) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("drakenrijken", name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(new Item.Properties().setId(key)));
	}
}
