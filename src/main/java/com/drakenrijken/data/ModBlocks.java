package com.drakenrijken.data;

import com.drakenrijken.DrakenRijken;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Blokken van de mod. Nu alleen het waterrad (DR-403); de kracht komt uit core.WaterWheel. */
public final class ModBlocks {
	public static Block WATERRAD;
	public static Block SMEDERIJ;

	private ModBlocks() {}

	public static void init() {
		WATERRAD = register("waterrad", WaterWheelBlock::new, BlockBehaviour.Properties.of().strength(2.0f).noOcclusion());
		SMEDERIJ = register("smederij", Block::new, BlockBehaviour.Properties.of().strength(3.5f));
	}

	private static Block register(String naam, java.util.function.Function<BlockBehaviour.Properties, Block> maker, BlockBehaviour.Properties props) {
		Identifier id = Identifier.fromNamespaceAndPath(DrakenRijken.MOD_ID, naam);
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
		Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, maker.apply(props.setId(blockKey)));
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
		return block;
	}
}
