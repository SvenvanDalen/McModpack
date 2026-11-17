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
			String t = tracker;
			if (t.isEmpty()) return;
			var font = Minecraft.getInstance().font;
			g.text(font, t, 6, 6, 0xFFFFFFFF, true);
		});
	}
}
