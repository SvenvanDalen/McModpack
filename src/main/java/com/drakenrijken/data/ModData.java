package com.drakenrijken.data;

import com.drakenrijken.DrakenRijken;
import com.drakenrijken.core.BuildCounter;
import com.drakenrijken.core.PlayerData;
import com.drakenrijken.core.XpRules;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;

/** Dunne Minecraft-laag: slaat PlayerData op bij de speler en voedt de xp-regels uit core/. */
public final class ModData {
	public static final AttachmentType<PlayerData> PLAYER_DATA = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath(DrakenRijken.MOD_ID, "player_data"),
		b -> b.persistent(PlayerDataCodec.CODEC).copyOnDeath().initializer(PlayerData::empty));

	/** Bouwtellers staan alleen in het geheugen; een halfvol tiental hoeft niet bewaard te blijven. */
	private static final java.util.Map<java.util.UUID, BuildCounter> BUILD = new java.util.concurrent.ConcurrentHashMap<>();

	private ModData() {}

	public static PlayerData of(ServerPlayer player) {
		return player.getAttachedOrCreate(PLAYER_DATA);
	}

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (source.getEntity() instanceof ServerPlayer player && !(entity instanceof ServerPlayer)) {
				XpRules.award(of(player).progress(), XpRules.mobKill(entity.getMaxHealth()));
			}
		});
		UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
			if (player instanceof ServerPlayer sp && sp.getItemInHand(hand).getItem() instanceof BlockItem) {
				int xp = BUILD.computeIfAbsent(sp.getUUID(), k -> new BuildCounter()).place();
				if (xp > 0) {
					XpRules.award(of(sp).progress(), xp);
				}
			}
			return InteractionResult.PASS;
		});
	}
}
