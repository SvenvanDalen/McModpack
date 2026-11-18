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
	public static Block WINDMOLEN;
	public static Block SMEDERIJ;
	public static Block AS;
	public static Block STOOMKETEL;
	public static Block ZAAG;
	public static Block PERS;
	public static Block RUNESTEEN;
	public static Block TRANSPORTBAND;
	public static Block RUNESMEDERIJ;
	public static Block BELASTINGKIST;
	public static Block BOUWPLANTAFEL;
	public static Block BLIKSEMAFLEIDER;
	public static Block GERUCHTENTAFEL;
	public static Block RUINESTEEN;
	public static Block MACHINEBORD;
	public static Block GLAZENBUIS;

	private ModBlocks() {}

	public static void init() {
		WATERRAD = register("waterrad", WaterWheelBlock::new, BlockBehaviour.Properties.of().strength(2.0f).noOcclusion());
		WINDMOLEN = register("windmolen", WindMillBlock::new, BlockBehaviour.Properties.of().strength(2.0f).noOcclusion());
		SMEDERIJ = register("smederij", p -> new MachineBlock(p, "smederij", 1), BlockBehaviour.Properties.of().strength(3.5f));
		AS = register("as", Block::new, BlockBehaviour.Properties.of().strength(1.5f));
		STOOMKETEL = register("stoomketel", SteamBoilerBlock::new, BlockBehaviour.Properties.of().strength(4.0f));
		ZAAG = register("zaag", p -> new MachineBlock(p, "zaag", 2), BlockBehaviour.Properties.of().strength(3.5f));
		PERS = register("pers", p -> new MachineBlock(p, "pers", 2), BlockBehaviour.Properties.of().strength(3.5f));
		RUNESTEEN = register("runesteen", RuneStoneBlock::new, BlockBehaviour.Properties.of().strength(4.0f));
		TRANSPORTBAND = register("transportband", ConveyorBlock::new, BlockBehaviour.Properties.of().strength(1.5f));
		RUNESMEDERIJ = register("runesmederij", p -> new MachineBlock(p, "runesmederij", 3), BlockBehaviour.Properties.of().strength(4.0f));
		BELASTINGKIST = register("belastingkist", TaxChestBlock::new, BlockBehaviour.Properties.of().strength(2.5f));
		BOUWPLANTAFEL = register("bouwplantafel", BlueprintTableBlock::new, BlockBehaviour.Properties.of().strength(2.5f));
		BLIKSEMAFLEIDER = register("bliksemafleider", LightningRodBlock::new, BlockBehaviour.Properties.of().strength(2.0f).randomTicks());
		GERUCHTENTAFEL = register("geruchtentafel", RumorTableBlock::new, BlockBehaviour.Properties.of().strength(2.0f));
		RUINESTEEN = register("ruinesteen", RuinStoneBlock::new, BlockBehaviour.Properties.of().strength(2.5f));
		MACHINEBORD = register("machinebord", MachineBoardBlock::new, BlockBehaviour.Properties.of().strength(2.0f));
		GLAZENBUIS = register("glazenbuis", GlassPipeBlock::new, BlockBehaviour.Properties.of().strength(0.8f).noOcclusion());
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
