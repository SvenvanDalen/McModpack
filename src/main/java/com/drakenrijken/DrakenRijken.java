package com.drakenrijken;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrakenRijken implements ModInitializer {
	public static final String MOD_ID = "drakenrijken";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** Ingelezen bij het starten. */
	public static com.drakenrijken.config.ModConfig config = com.drakenrijken.config.ModConfig.defaults();

	@Override
	public void onInitialize() {
		com.drakenrijken.data.ModBlocks.init();
		com.drakenrijken.data.ModData.init();
		com.drakenrijken.data.ModSkills.init();
		com.drakenrijken.data.ModKits.init();
		com.drakenrijken.data.ModRecipes.init();
		com.drakenrijken.data.DebugCommand.init();
		com.drakenrijken.quest.QuestEvents.init();
		config = com.drakenrijken.config.ConfigFile.load(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir());
		LOGGER.info("Draken en Rijken geladen");
	}
}
