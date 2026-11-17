package com.drakenrijken.client;

import com.drakenrijken.core.PlayerClass;
import com.drakenrijken.core.Skill;
import com.drakenrijken.data.ModSkills;
import java.util.Arrays;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Vaardighedenscherm (DR-106): alle vaardigheden per klasse als knoppen.
 * Een klik stuurt /dr vaardigheid &lt;id&gt; naar de server; die bepaalt of het mag en meldt het in de chat.
 * De server stuurt de punten en geleerde vaardigheden mee ("punten;id1,id2"); geleerde staan met [x] en zijn uitgeschakeld.
 */
public class SkillScreen extends Screen {
	private String gezien = "";

	public SkillScreen() {
		super(Component.literal("Vaardigheden"));
	}

	private static String[] delen() {
		String[] d = DrakenRijkenClient.skills.split(";", -1);
		return d.length == 2 ? d : new String[] {"0", ""};
	}

	@Override
	protected void init() {
		gezien = DrakenRijkenClient.skills;
		Set<String> geleerd = Set.copyOf(Arrays.asList(delen()[1].split(",")));
		int x = 10;
		for (PlayerClass klasse : PlayerClass.values()) {
			int y = 30;
			for (Skill s : ModSkills.tree().skillsFor(klasse)) {
				String eerst = s.requires().isEmpty() ? "geen" : String.join(", ", s.requires());
				boolean af = geleerd.contains(s.id());
				var knop = Button.builder(Component.literal((af ? "[x] " : "") + s.id()), b -> {
						var verbinding = Minecraft.getInstance().getConnection();
						if (verbinding != null) verbinding.sendCommand("dr vaardigheid " + s.id());
					})
					.bounds(x, y, 90, 20)
					.tooltip(Tooltip.create(Component.literal(klasse.name() + " - eerst nodig: " + eerst)))
					.build();
				knop.active = !af;
				addRenderableWidget(knop);
				y += 22;
			}
			x += 95;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!gezien.equals(DrakenRijkenClient.skills)) rebuildWidgets();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
		super.extractRenderState(g, mouseX, mouseY, delta);
		g.text(font, "Vaardigheden - punten over: " + delen()[0], 10, 10, 0xFFFFFFFF);
	}

	@Override
	public boolean isPauseScreen() { return false; }
}
