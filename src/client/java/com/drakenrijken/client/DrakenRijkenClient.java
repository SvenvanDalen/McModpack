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
	/** "punten;id1,id2", gevuld door de server; gebruikt door het vaardighedenscherm. */
	public static volatile String skills = "0;";

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(TrackerPayload.TYPE, (payload, ctx) -> tracker = payload.text());
		ClientPlayNetworking.registerGlobalReceiver(com.drakenrijken.data.SkillsPayload.TYPE, (payload, ctx) -> skills = payload.text());
		var vaardigheidsToets = net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping(
			new net.minecraft.client.KeyMapping("key.drakenrijken.vaardigheden", 75, net.minecraft.client.KeyMapping.Category.MISC));
		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			while (vaardigheidsToets.consumeClick()) {
				if (mc.player != null) mc.setScreenAndShow(new SkillScreen());
			}
		});
		HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.fromNamespaceAndPath("drakenrijken", "tracker"), (g, delta) -> {
			String[] regels = tracker.split("\n");
			if (tracker.isEmpty()) return;
			var font = Minecraft.getInstance().font;
			for (int i = 0; i < regels.length; i++) g.text(font, regels[i], 6, 6 + i * 10, 0xFFFFFFFF, true);
		});
	}
}
