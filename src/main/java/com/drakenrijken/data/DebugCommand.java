package com.drakenrijken.data;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.PlayerData;
import com.drakenrijken.core.QuestLog;
import com.drakenrijken.quest.QuestEvents;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/** Commando's /dr debug, /dr klasse en /dr quests, plus de klassekeuze-uitnodiging bij het inloggen. */
public final class DebugCommand {
	private DebugCommand() {}

	public static void init() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			if (ModData.of(handler.getPlayer()).playerClass().isEmpty()) {
				handler.getPlayer().sendSystemMessage(Component.literal(
					"Kies je klasse met /dr klasse ridder, /dr klasse magier of /dr klasse schurk."));
			}
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, ctx, sel) ->
			dispatcher.register(Commands.literal("dr")
				.then(Commands.literal("debug").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					PlayerData d = ModData.of(p);
					String text = "klasse=" + d.playerClass().map(Enum::name).orElse("geen")
						+ " level=" + d.progress().level() + " xp=" + d.progress().xp()
						+ " punten=" + ModSkills.tree().pointsLeft(d.progress())
						+ " vaardigheden=" + d.progress().unlocked()
						+ " quests klaar=" + d.quests().completed() + " bezig=" + d.quests().counts();
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				}))
				.then(Commands.literal("klasse").then(Commands.argument("naam", StringArgumentType.word()).executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					PlayerData d = ModData.of(p);
					if (d.playerClass().isPresent()) {
						c.getSource().sendFailure(Component.literal("Je hebt al een klasse: " + d.playerClass().get().name().toLowerCase()));
						return 0;
					}
					var klasse = PlayerClass.parse(StringArgumentType.getString(c, "naam"));
					if (klasse.isEmpty()) {
						c.getSource().sendFailure(Component.literal("Onbekende klasse. Kies ridder, magier of schurk."));
						return 0;
					}
					d.setPlayerClass(klasse.get());
					p.setAttached(ModData.PLAYER_DATA, d);
					ModKits.give(p, klasse.get());
					c.getSource().sendSuccess(() -> Component.literal("Je bent nu " + klasse.get().name().toLowerCase() + "."), false);
					return 1;
				})))
				.then(Commands.literal("tijdperk").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					int era = new com.drakenrijken.core.EraGate("poort_stoom", "poort_runen").currentEra(ModData.of(p).quests());
					c.getSource().sendSuccess(() -> Component.literal("Je zit in tijdperk " + era + " van 3."), false);
					return 1;
				}))
				.then(Commands.literal("rijk").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					int macht = RealmScan.powerAround(p.level(), p.blockPosition());
					PlayerData rd = ModData.of(p);
					rd.setRealmPower(macht);
					p.setAttached(ModData.PLAYER_DATA, rd);
					int nieuw = p.level() instanceof net.minecraft.server.level.ServerLevel sl
						? RealmScan.topUpVillagers(sl, p.blockPosition(), com.drakenrijken.core.RealmState.levelFor(macht)) : 0;
					String text = com.drakenrijken.core.RealmReport.describe(macht) + (nieuw > 0 ? " " + nieuw + " nieuwe dorpelingen." : "");
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				}))
				.then(Commands.literal("aanval").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					int n = p.level() instanceof net.minecraft.server.level.ServerLevel sl
						? RealmRaid.launch(sl, p, com.drakenrijken.core.RealmState.levelFor(ModData.of(p).realmPower())) : 0;
					if (n == 0) c.getSource().sendSuccess(() -> Component.literal("Geen aanval: je rijk is nog te klein (niveau 2 nodig) of er zijn al genoeg vijanden."), false);
					return 1;
				}))
				.then(Commands.literal("draak").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					boolean ok = p.level() instanceof net.minecraft.server.level.ServerLevel sl && DragonBoss.spawn(sl, p.blockPosition().offset(8, 0, 0));
					c.getSource().sendSuccess(() -> Component.literal(ok ? "De draak is wakker geworden!" : "Er is al een draak."), false);
					return 1;
				}))
				.then(Commands.literal("handleiding").executes(c -> {
					c.getSource().sendSuccess(() -> Component.literal(ModHandbook.book().index()), false);
					return 1;
				}).then(Commands.argument("onderwerp", StringArgumentType.word()).executes(c -> {
					var h = ModHandbook.book();
					String text = h.find(StringArgumentType.getString(c, "onderwerp")).map(h::describe).orElse("Onbekend onderwerp.\n" + h.index());
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				})))
				.then(Commands.literal("recepten").executes(c -> {
					int n = ModRecipes.book().all().size();
					c.getSource().sendSuccess(() -> Component.literal(n + " machinerecepten geladen."), false);
					return 1;
				}))
				.then(Commands.literal("quests").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					var lines = QuestLog.lines(QuestEvents.engine().quests(), ModData.of(p).quests());
					String text = lines.isEmpty() ? "Geen quests." : String.join("\n", lines);
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				}))));
	}
}
