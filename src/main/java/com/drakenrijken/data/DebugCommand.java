package com.drakenrijken.data;

import com.drakenrijken.core.PlayerData;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/** /dr debug: toont klasse, level, xp, vaardigheden en quests van de speler. */
public final class DebugCommand {
	private DebugCommand() {}

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, ctx, sel) ->
			dispatcher.register(Commands.literal("dr").then(Commands.literal("debug").executes(c -> {
				ServerPlayer p = c.getSource().getPlayerOrException();
				PlayerData d = ModData.of(p);
				String text = "klasse=" + d.playerClass().map(Enum::name).orElse("geen")
					+ " level=" + d.progress().level() + " xp=" + d.progress().xp()
					+ " punten=" + ModSkills.tree().pointsLeft(d.progress())
					+ " vaardigheden=" + d.progress().unlocked()
					+ " quests klaar=" + d.quests().completed() + " bezig=" + d.quests().counts();
				c.getSource().sendSuccess(() -> Component.literal(text), false);
				return 1;
			}))));
	}
}
