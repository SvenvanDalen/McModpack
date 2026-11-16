package com.drakenrijken.data;

import com.drakenrijken.core.RealmState;
import com.drakenrijken.core.RealmTrade;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.villager.Villager;

/**
 * Betere prijzen bij een hoger rijk (DR-504). Zonder mixin: dorpelingen kennen al een reputatie die prijzen
 * verlaagt, dus we zetten die voor de handel op het niveau van het rijk. De korting is een benadering, geen exact percentage.
 */
public final class RealmTrading {
	private RealmTrading() {}

	public static void init() {
		UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
			if (player instanceof ServerPlayer sp && entity instanceof Villager v) {
				apply(v, sp, RealmState.levelFor(ModData.of(sp).realmPower()));
			}
			return InteractionResult.PASS;
		});
	}

	/** Zet de welwillendheid van deze dorpeling voor deze speler op het niveau van het rijk. */
	public static void apply(Villager villager, ServerPlayer player, int realmLevel) {
		var gossips = villager.getGossips();
		gossips.remove(player.getUUID(), GossipType.MAJOR_POSITIVE);
		int goodwill = RealmTrade.goodwill(realmLevel);
		if (goodwill > 0) gossips.add(player.getUUID(), GossipType.MAJOR_POSITIVE, goodwill);
	}
}
