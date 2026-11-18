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
				.then(Commands.literal("beroepen").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					int niveau = com.drakenrijken.core.RealmState.levelFor(ModData.of(p).realmPower());
					int[] r = com.drakenrijken.core.Profession.assign(com.drakenrijken.core.RealmVillagers.target(niveau));
					StringBuilder sb = new StringBuilder("Beroepen in je rijk:");
					for (var b : com.drakenrijken.core.Profession.values()) sb.append(' ').append(b.naam()).append(' ').append(r[b.ordinal()]).append(',');
					sb.append(" opbrengst ").append(com.drakenrijken.core.Profession.dailyYield(niveau)).append(" per dag (nog niet uitbetaald).");
					c.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
					return 1;
				}))
				.then(Commands.literal("seizoen").executes(c -> {
					long dag = c.getSource().getLevel().getGameTime() / 24000L;
					var s = com.drakenrijken.core.Season.of(dag);
					String text = s.naam() + ", nog " + com.drakenrijken.core.Season.daysLeft(dag) + " dag(en). Oogstfactor " + s.harvestPercent() + "% (nog niet aan de oogst gekoppeld).";
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				}))
				.then(Commands.literal("belasting").executes(c -> {
					String text = TaxChestBlock.collect(c.getSource().getPlayerOrException());
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				}))
				.then(Commands.literal("draak").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					boolean ok = p.level() instanceof net.minecraft.server.level.ServerLevel sl && DragonBoss.spawn(sl, p.blockPosition().offset(8, 0, 0));
					c.getSource().sendSuccess(() -> Component.literal(ok ? "De draak is wakker geworden!" : "Er is al een draak."), false);
					return 1;
				}))
				.then(Commands.literal("wachter").executes(c -> {
					ServerPlayer p = c.getSource().getPlayerOrException();
					int era = new com.drakenrijken.core.EraGate("poort_stoom", "poort_runen").currentEra(ModData.of(p).quests());
					boolean ok = era >= 3 && p.level() instanceof net.minecraft.server.level.ServerLevel sl
						&& RunewardenBoss.spawn(sl, p.blockPosition().offset(8, 0, 0));
					String text = era < 3 ? "De Runenwachter wacht pas in tijdperk 3."
						: ok ? "De Runenwachter is wakker geworden!" : "Er is al een Runenwachter.";
					c.getSource().sendSuccess(() -> Component.literal(text), false);
					return 1;
				}))
					.then(Commands.literal("drakengebied").executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						int n = p.level() instanceof net.minecraft.server.level.ServerLevel sl
							? placeWithGuards(sl, p.blockPosition().offset(4, 0, -3), com.drakenrijken.core.Blueprints.lair(), "ruine", StructureBuilder.eraOf(p)) : 0;
						c.getSource().sendSuccess(() -> Component.literal("Drakengebied gebouwd (" + n + " blokken), vlak voor je."), false);
						return 1;
					}))
					.then(Commands.literal("burcht").executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						int n = p.level() instanceof net.minecraft.server.level.ServerLevel sl
							? placeWithGuards(sl, p.blockPosition().offset(4, 6, -3), com.drakenrijken.core.Blueprints.castle(), "burcht", StructureBuilder.eraOf(p)) : 0;
						c.getSource().sendSuccess(() -> Component.literal("Burcht gebouwd (" + n + " blokken), vlak voor je."), false);
						return 1;
					}))
					.then(Commands.literal("dorp").executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						int n = p.level() instanceof net.minecraft.server.level.ServerLevel sl
							? StructureBuilder.place(sl, p.blockPosition().offset(4, 0, -3), com.drakenrijken.core.Blueprints.village()) : 0;
						c.getSource().sendSuccess(() -> Component.literal("Dorp gebouwd (" + n + " blokken), vlak voor je."), false);
						return 1;
					}))
					.then(Commands.literal("dungeon").executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						int n = p.level() instanceof net.minecraft.server.level.ServerLevel sl
							? placeWithGuards(sl, p.blockPosition().offset(4, 0, -3), com.drakenrijken.core.Blueprints.dungeon(), "dungeon", StructureBuilder.eraOf(p)) : 0;
						c.getSource().sendSuccess(() -> Component.literal("Dungeon gebouwd (" + n + " blokken), vlak voor je."), false);
						return 1;
					}))
						.then(Commands.literal("reis").then(Commands.argument("nummer", com.mojang.brigadier.arguments.IntegerArgumentType.integer(1)).executes(c -> {
							ServerPlayer p = c.getSource().getPlayerOrException();
							String text = p.level() instanceof net.minecraft.server.level.ServerLevel sl
								? FastTravelService.travel(sl, p, com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(c, "nummer")) : "Reizen kan alleen in de bovenwereld.";
							c.getSource().sendSuccess(() -> Component.literal(text), false);
							return 1;
						})))
					.then(Commands.literal("kaart").executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						if (p.level() instanceof net.minecraft.server.level.ServerLevel sl) WorldDiscovery.visit(sl, p);
						String text = WorldDiscovery.describe(p);
						c.getSource().sendSuccess(() -> Component.literal(text), false);
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
					.then(Commands.literal("vaardigheid")
						.executes(c -> {
							ServerPlayer p = c.getSource().getPlayerOrException();
							PlayerData d = ModData.of(p);
							if (d.playerClass().isEmpty()) {
								c.getSource().sendFailure(Component.literal("Kies eerst een klasse met /dr klasse."));
								return 0;
							}
							var tree = ModSkills.tree();
							var sb = new StringBuilder("Punten over: " + tree.pointsLeft(d.progress()));
							for (var s : tree.skillsFor(d.playerClass().get())) {
								sb.append("\n").append(d.progress().unlocked().contains(s.id()) ? "[x] " : "[ ] ").append(s.id());
								if (!s.requires().isEmpty()) sb.append(" (eerst: ").append(String.join(", ", s.requires())).append(")");
							}
							c.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
							return 1;
						})
						.then(Commands.argument("id", StringArgumentType.word()).executes(c -> {
							ServerPlayer p = c.getSource().getPlayerOrException();
							PlayerData d = ModData.of(p);
							if (d.playerClass().isEmpty()) {
								c.getSource().sendFailure(Component.literal("Kies eerst een klasse met /dr klasse."));
								return 0;
							}
							String id = StringArgumentType.getString(c, "id");
							var r = ModSkills.tree().unlock(d.progress(), d.playerClass().get(), id);
							p.setAttached(ModData.PLAYER_DATA, d);
							String text = switch (r) {
								case OK -> "Vaardigheid " + id + " ontgrendeld.";
								case NO_POINTS -> "Je hebt geen punten meer.";
								case LOCKED -> "Ontgrendel eerst de vereiste vaardigheid.";
								case UNKNOWN -> "Onbekende vaardigheid. Zie /dr vaardigheid.";
								case ALREADY_UNLOCKED -> "Die heb je al.";
								case WRONG_CLASS -> "Die hoort bij een andere klasse.";
							};
							if (r == com.drakenrijken.core.SkillTree.Result.OK) c.getSource().sendSuccess(() -> Component.literal(text), false);
							else c.getSource().sendFailure(Component.literal(text));
							return r == com.drakenrijken.core.SkillTree.Result.OK ? 1 : 0;
						})))
					.then(Commands.literal("gebruik").then(Commands.argument("id", StringArgumentType.word()).executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						PlayerData d = ModData.of(p);
						if (d.playerClass().isEmpty()) {
							c.getSource().sendFailure(Component.literal("Kies eerst een klasse met /dr klasse."));
							return 0;
						}
						String id = StringArgumentType.getString(c, "id");
						var r = com.drakenrijken.core.SkillCosts.use(d.progress(), QuestTrackerSync.barOf(p, d.playerClass().get()), id);
						switch (r) {
							case PASSIVE -> c.getSource().sendFailure(Component.literal("Dit is geen vaardigheid die je kunt gebruiken (nu: vuurbal, oorlogskreet, barriere, schildstoot, schaduwstap, ijsstraal, vuurmuur, gifpijl, bloedwraak, sluipaanval, dubbele_buit, zakkenroller, manaschild)."));
							case NOT_UNLOCKED -> c.getSource().sendFailure(Component.literal("Die vaardigheid heb je nog niet ontgrendeld."));
							case TOO_TIRED -> c.getSource().sendFailure(Component.literal("Te weinig mana of uithouding."));
							case OK -> {
								if (id.equals("vuurbal") && p.level() instanceof net.minecraft.server.level.ServerLevel sl) {
									var bal = new net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball(sl, p, p.getLookAngle());
									bal.setPos(p.getX(), p.getEyeY(), p.getZ());
									sl.addFreshEntity(bal);
								}
								if (id.equals("oorlogskreet")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.STRENGTH, 200, 0));
								if (id.equals("barriere")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.ABSORPTION, 600, 1));
								if (id.equals("schildstoot")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.RESISTANCE, 100, 1));
								if (id.equals("schaduwstap")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.INVISIBILITY, 160, 0));
								if (id.equals("bloedwraak")) p.heal(6f);
								if (id.equals("sluipaanval")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.STRENGTH, 100, 1));
								if (id.equals("dubbele_buit")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LUCK, 1200, 1));
								if (id.equals("zakkenroller")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.LUCK, 600, 3));
								if (id.equals("manaschild")) p.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.ABSORPTION, 400, 2));
								if (id.equals("gifpijl") && p.level() instanceof net.minecraft.server.level.ServerLevel gl) {
									var dichtst = gl.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, p.getBoundingBox().inflate(8), e -> e != p && e.isAlive())
										.stream().min(java.util.Comparator.comparingDouble(e -> e.distanceTo(p)));
									dichtst.ifPresent(e -> e.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.POISON, 100, 1)));
								}
								if ((id.equals("ijsstraal") || id.equals("vuurmuur")) && p.level() instanceof net.minecraft.server.level.ServerLevel zl) {
									double straal = id.equals("ijsstraal") ? 6 : 4;
									var doelen = zl.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class, p.getBoundingBox().inflate(straal), e -> e != p && e.isAlive());
									for (var e : doelen) {
										if (id.equals("ijsstraal")) e.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.SLOWNESS, 100, 2));
										else e.igniteForSeconds(4);
									}
								}
								var geluid = switch (id) {
										case "vuurbal", "vuurmuur" -> net.minecraft.sounds.SoundEvents.BLAZE_SHOOT;
										case "oorlogskreet" -> net.minecraft.sounds.SoundEvents.ENDER_DRAGON_GROWL;
										case "schildstoot", "barriere" -> net.minecraft.sounds.SoundEvents.ANVIL_USE;
										case "bloedwraak" -> net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP;
										default -> net.minecraft.sounds.SoundEvents.EVOKER_CAST_SPELL;
									};
									p.level().playSound(null, p.blockPosition(), geluid, net.minecraft.sounds.SoundSource.PLAYERS, 1f, 1f);
									c.getSource().sendSuccess(() -> Component.literal(id.equals("vuurbal") ? "Vuurbal!" : id.equals("oorlogskreet") ? "Oorlogskreet!" : id.equals("barriere") ? "Barrière!" : id.equals("schildstoot") ? "Schildstoot!" : id.equals("schaduwstap") ? "Schaduwstap!" : id.equals("ijsstraal") ? "Ijsstraal!" : id.equals("vuurmuur") ? "Vuurmuur!" : id.equals("gifpijl") ? "Gifpijl!" : id.equals("bloedwraak") ? "Bloedwraak!" : id.equals("sluipaanval") ? "Sluipaanval!" : id.equals("dubbele_buit") ? "Dubbele buit!" : id.equals("zakkenroller") ? "Zakkenroller!" : "Manaschild!"), false);
							}
						}
						return r == com.drakenrijken.core.SkillCosts.Result.OK ? 1 : 0;
					})))
					.then(Commands.literal("tracker").executes(c -> {
						ServerPlayer p = c.getSource().getPlayerOrException();
						boolean aan = QuestTrackerSync.toggle(p);
						c.getSource().sendSuccess(() -> Component.literal(aan ? "Quest-tracker staat aan." : "Quest-tracker staat uit."), false);
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
	private static int placeWithGuards(net.minecraft.server.level.ServerLevel sl, net.minecraft.core.BlockPos hoek, com.drakenrijken.core.Blueprint plan, String tabel, int era) {
		int n = StructureBuilder.placeWithLoot(sl, hoek, plan, tabel, era);
		StructureBuilder.spawnGuards(sl, hoek, plan, tabel.equals("ruine") ? "drakengebied" : "dungeon", era);
		return n;
	}
}
