package com.drakenrijken;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrakenRijken implements ModInitializer {
	public static final String MOD_ID = "drakenrijken";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		com.drakenrijken.data.ModData.init();
		LOGGER.info("Draken en Rijken geladen");
	}
}
