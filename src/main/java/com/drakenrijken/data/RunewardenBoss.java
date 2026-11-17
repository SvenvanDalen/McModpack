package com.drakenrijken.data;

import com.drakenrijken.core.BossPhases;
import com.drakenrijken.core.BossRewards;
import com.drakenrijken.core.WardenStats;
import java.util.List;
import java.util.UUID;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

/**
 * De Runenwachter (DR-610c), optionele baas voor tijdperk 3. Voorlopig een vanilla ijzergolem als stand-in:
 * met balk, drie fasen, schokgolven, herstel als niemand vecht en een eenmalige beloning.
 */
public final class RunewardenBoss {
	private static final int RANGE = 48;
	private static final BossRewards REWARDS = new BossRewards();
	private static LivingEntity warden;
	private static ServerBossEvent bar;
	private static int phase = 0;

	private RunewardenBoss() {}

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (warden != null && server.getTickCount() % 10 == 0) tick();
		});
	}

	public static boolean active() { return warden != null; }

	public static boolean spawn(ServerLevel level, BlockPos pos) {
		if (warden != null) return false;
		var g = EntityTypes.IRON_GOLEM.spawn(level, pos, EntitySpawnReason.EVENT);
		if (g == null) return false;
		g.setCustomName(Component.literal("Runenwachter"));
		g.getAttribute(Attributes.MAX_HEALTH).setBaseValue(WardenStats.BASE_HEALTH);
		g.setHealth(WardenStats.BASE_HEALTH);
		g.setPersistenceRequired();
		warden = g;
		bar = new ServerBossEvent(UUID.randomUUID(), Component.literal("Runenwachter"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
		phase = 1;
		return true;
	}

	public static void tick() {
		if (!(warden.level() instanceof ServerLevel level)) return;
		var area = new AABB(warden.blockPosition()).inflate(RANGE);
		List<ServerPlayer> spelers = level.getEntitiesOfClass(ServerPlayer.class, area, p -> p.isAlive() && !p.isSpectator());
		if (!warden.isAlive()) {
			for (ServerPlayer p : spelers) {
				if (REWARDS.rewardOnce(p.getUUID().toString())) {
					p.getInventory().add(new ItemStack(Items.DIAMOND, 3));
					p.sendSystemMessage(Component.literal("De Runenwachter is verslagen! Je krijgt 3 diamanten."));
				}
			}
			stop();
			return;
		}
		for (ServerPlayer p : spelers) bar.addPlayer(p);
		for (ServerPlayer p : List.copyOf(bar.getPlayers())) if (!spelers.contains(p)) bar.removePlayer(p);
		if (spelers.isEmpty()) warden.setHealth(warden.getMaxHealth());
		float fractie = Math.max(0f, Math.min(1f, warden.getHealth() / warden.getMaxHealth()));
		bar.setProgress(fractie);
		phase = BossPhases.phaseFor(fractie);
		bar.setName(Component.literal("Runenwachter - fase " + phase));
		if (warden.tickCount % WardenStats.shockInterval(phase) < 10) {
			float schade = (float) (WardenStats.SHOCK_DAMAGE * WardenStats.damageFactor(phase));
			for (ServerPlayer p : spelers) {
				if (p.distanceTo(warden) <= WardenStats.SHOCK_RANGE) p.hurtServer(level, warden.damageSources().mobAttack(warden), schade);
			}
		}
	}

	public static void stop() {
		if (bar != null) bar.removeAllPlayers();
		if (warden != null && warden.isAlive()) warden.discard();
		warden = null;
		bar = null;
	}
}
