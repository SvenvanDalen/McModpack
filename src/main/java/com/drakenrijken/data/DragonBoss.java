package com.drakenrijken.data;

import com.drakenrijken.core.BossPhases;
import com.drakenrijken.core.BossRewards;
import com.drakenrijken.core.DragonStats;
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
 * De draak (DR-603 tot 608). Voorlopig een vanilla Ravager als stand-in zonder eigen model (DR-602 volgt):
 * met gezondheidsbalk, drie fasen, vuur vanaf fase 2, herstel als niemand meer vecht en een eenmalige beloning.
 */
public final class DragonBoss {
	private static final int RANGE = 48;
	private static final double BASE_SPEED = 0.35;
	private static final double BASE_DAMAGE = 12;
	private static final BossRewards REWARDS = new BossRewards();
	private static LivingEntity dragon;
	private static ServerBossEvent bar;
	private static int phase = 1;

	private DragonBoss() {}

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (dragon != null && server.getTickCount() % 10 == 0) tick();
		});
	}

	public static boolean active() { return dragon != null; }

	public static int phase() { return phase; }

	public static float healthFraction() { return dragon == null ? 0f : dragon.getHealth() / dragon.getMaxHealth(); }

	public static void damageForTest(float amount) { dragon.setHealth(dragon.getHealth() - amount); }

	public static boolean spawn(ServerLevel level, BlockPos pos) {
		if (dragon != null) return false;
		var r = EntityTypes.RAVAGER.spawn(level, pos, EntitySpawnReason.EVENT);
		if (r == null) return false;
		r.setCustomName(Component.literal("Draak"));
		r.getAttribute(Attributes.MAX_HEALTH).setBaseValue(DragonStats.BASE_HEALTH);
		r.setHealth(DragonStats.BASE_HEALTH);
		r.setPersistenceRequired();
		dragon = r;
		bar = new ServerBossEvent(UUID.randomUUID(), Component.literal("Draak"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
		phase = 0;
		applyPhase(1);
		return true;
	}

	private static void applyPhase(int nieuw) {
		if (nieuw == phase) return;
		phase = nieuw;
		dragon.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_SPEED * DragonStats.speedFactor(nieuw));
		dragon.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_DAMAGE * DragonStats.damageFactor(nieuw));
		bar.setName(Component.literal("Draak - fase " + nieuw));
		bar.setColor(nieuw == 3 ? BossEvent.BossBarColor.PURPLE : nieuw == 2 ? BossEvent.BossBarColor.YELLOW : BossEvent.BossBarColor.RED);
	}

	/** Eén controleronde; apart zodat een test hem direct kan aanroepen. */
	public static void tick() {
		if (!(dragon.level() instanceof ServerLevel level)) return;
		var area = new AABB(dragon.blockPosition()).inflate(RANGE);
		List<ServerPlayer> spelers = level.getEntitiesOfClass(ServerPlayer.class, area, p -> p.isAlive() && !p.isSpectator());
		if (!dragon.isAlive()) {
			beloon(spelers);
			stop();
			return;
		}
		for (ServerPlayer p : spelers) bar.addPlayer(p);
		for (ServerPlayer p : List.copyOf(bar.getPlayers())) if (!spelers.contains(p)) bar.removePlayer(p);
		// Herstel (DR-608): niemand vecht meer, dus de draak wordt weer heel en geeft geen beloning.
		if (spelers.isEmpty()) dragon.setHealth(dragon.getMaxHealth());
		float fractie = Math.max(0f, Math.min(1f, dragon.getHealth() / dragon.getMaxHealth()));
		bar.setProgress(fractie);
		applyPhase(BossPhases.phaseFor(fractie));
		if (dragon.tickCount % DragonStats.TAIL_INTERVAL_TICKS < 10) {
			for (ServerPlayer p : spelers) if (p.distanceTo(dragon) <= DragonStats.TAIL_RANGE) {
				p.hurtServer(level, dragon.damageSources().mobAttack(dragon), DragonStats.TAIL_DAMAGE);
			}
		}
		if (DragonStats.breathesFire(phase) && dragon.tickCount % DragonStats.fireInterval(phase) < 10) {
			for (ServerPlayer p : spelers) if (p.distanceTo(dragon) <= DragonStats.FIRE_RANGE) p.igniteForSeconds(3);
		}
	}

	private static void beloon(List<ServerPlayer> spelers) {
		for (ServerPlayer p : spelers) {
			if (REWARDS.rewardOnce(p.getUUID().toString())) {
				p.getInventory().add(new ItemStack(Items.DIAMOND, 5));
				p.sendSystemMessage(Component.literal("De draak is verslagen! Je krijgt 5 diamanten."));
			}
		}
	}

	/** Haalt de draak en zijn balk weg. */
	public static void stop() {
		if (bar != null) bar.removeAllPlayers();
		if (dragon != null && dragon.isAlive()) dragon.discard();
		dragon = null;
		bar = null;
	}
}
