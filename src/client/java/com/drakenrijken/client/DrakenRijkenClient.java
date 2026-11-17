package com.drakenrijken.client;

import com.drakenrijken.data.TrackerPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class DrakenRijkenClient implements ClientModInitializer {
	private static volatile String tracker = "";

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(TrackerPayload.TYPE, (payload, ctx) -> tracker = payload.text());
		HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.fromNamespaceAndPath("drakenrijken", "tracker"), (g, delta) -> {
			String[] regels = tracker.split("\n");
			if (tracker.isEmpty()) return;
			var font = Minecraft.getInstance().font;
			for (int i = 0; i < regels.length; i++) g.text(font, regels[i], 6, 6 + i * 10, 0xFFFFFFFF, true);
		});
	}
}
