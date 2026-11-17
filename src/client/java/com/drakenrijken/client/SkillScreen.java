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
	private final java.util.Map<String, int[]> plek = new java.util.HashMap<>();

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
		plek.clear();
		Set<String> geleerd = Set.copyOf(Arrays.asList(delen()[1].split(",")));
		int x = 10;
		for (PlayerClass klasse : PlayerClass.values()) {
			int y = 30;
			for (Skill s : ModSkills.tree().skillsFor(klasse)) {
				String eerst = s.requires().isEmpty() ? "geen" : String.join(", ", s.requires());
				boolean af = geleerd.contains(s.id());
				plek.put(s.id(), new int[] {x, y});
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
			x += 100;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!gezien.equals(DrakenRijkenClient.skills)) rebuildWidgets();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
		boolean hoog = com.drakenrijken.DrakenRijken.config.highContrast();
		if (hoog) g.fill(0, 0, width, height, 0xFF000000);
		super.extractRenderState(g, mouseX, mouseY, delta);
		// Lijnen van elke vereiste vaardigheid naar de vaardigheid die ze nodig heeft (linkerkant van de kolom).
		for (Skill s : allSkills()) {
			int[] naar = plek.get(s.id());
			for (String eis : s.requires()) {
				int[] van = plek.get(eis);
				if (naar == null || van == null || van[0] != naar[0]) continue;
				int lijnX = naar[0] - 4;
				g.horizontalLine(lijnX, naar[0] - 1, van[1] + 10, (hoog ? 0xFFFFFF00 : 0xFFAAAAAA));
				g.verticalLine(lijnX, Math.min(van[1], naar[1]) + 10, Math.max(van[1], naar[1]) + 10, (hoog ? 0xFFFFFF00 : 0xFFAAAAAA));
				g.horizontalLine(lijnX, naar[0] - 1, naar[1] + 10, (hoog ? 0xFFFFFF00 : 0xFFAAAAAA));
			}
		}
		float s = (float) com.drakenrijken.DrakenRijken.config.textScale();
		g.pose().pushMatrix();
		g.pose().scale(s, s);
		g.text(font, "Vaardigheden - punten over: " + delen()[0], Math.round(10 / s), Math.round(10 / s), 0xFFFFFFFF);
		g.pose().popMatrix();
	}


	private static java.util.List<Skill> allSkills() {
		var lijst = new java.util.ArrayList<Skill>();
		for (PlayerClass k : PlayerClass.values()) lijst.addAll(ModSkills.tree().skillsFor(k));
		return lijst;
	}
	@Override
	public boolean isPauseScreen() { return false; }
}
