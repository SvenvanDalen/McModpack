package com.drakenrijken.client;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.Skill;
import com.drakenrijken.data.ModSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Vaardighedenscherm (DR-106): alle vaardigheden per klasse als knoppen.
 * Een klik stuurt /dr vaardigheid &lt;id&gt; naar de server; die bepaalt of het mag en meldt het in de chat.
 */
public class SkillScreen extends Screen {
	public SkillScreen() {
		super(Component.literal("Vaardigheden"));
	}

	@Override
	protected void init() {
		int x = 10;
		for (PlayerClass klasse : PlayerClass.values()) {
			int y = 30;
			for (Skill s : ModSkills.tree().skillsFor(klasse)) {
				String eerst = s.requires().isEmpty() ? "geen" : String.join(", ", s.requires());
				addRenderableWidget(Button.builder(Component.literal(s.id()), b -> {
						var verbinding = Minecraft.getInstance().getConnection();
						if (verbinding != null) verbinding.sendCommand("dr vaardigheid " + s.id());
					})
					.bounds(x, y, 90, 20)
					.tooltip(Tooltip.create(Component.literal(klasse.name() + " - eerst nodig: " + eerst)))
					.build());
				y += 22;
			}
			x += 95;
		}
	}

	@Override
	public boolean isPauseScreen() { return false; }
}
