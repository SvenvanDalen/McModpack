package com.drakenrijken.data;

import com.drakenrijken.core.MachineJobs;
import com.drakenrijken.core.MachineRecipe;
import com.drakenrijken.core.MachineWear;
import com.drakenrijken.core.TickBudget;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/** Doorlopende machines (DR-802): een klik start een werk dat over meerdere ticks loopt; het resultaat valt bij het blok. Bij afsluiten wordt alles afgemaakt. */
public final class MachineRuntime {
	private static final MachineJobs JOBS = new MachineJobs();
	private static final TickBudget TIME = TickBudget.machines();
	private static final Map<String, MachineWear> WEAR = new HashMap<>();
	private static final java.util.Set<String> BOOSTED = new java.util.HashSet<>();

	private MachineRuntime() {}

	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (JOBS.count() == 0) return;
			TIME.begin();
			for (var f : JOBS.tick(k -> power(server, k), TIME)) deliver(server, f);
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			for (var f : JOBS.finishAll()) deliver(server, f);
		});
	}

	public static int active() { return JOBS.count(); }

	/** Voortgang (0 tot 100) van het werk op deze plek, of -1 als er niets loopt. */
	public static int percentAt(ServerLevel level, BlockPos pos) {
		String k = key(level, pos);
		return JOBS.running(k) ? JOBS.percent(k) : -1;
	}

	private static String key(ServerLevel level, BlockPos pos) { return level.dimension().identifier() + "|" + pos.asLong(); }

	private static ServerLevel levelOf(MinecraftServer server, String key) {
		String dim = key.substring(0, key.indexOf('|'));
		for (ServerLevel l : server.getAllLevels()) if (l.dimension().identifier().toString().equals(dim)) return l;
		return null;
	}

	private static BlockPos posOf(String key) { return BlockPos.of(Long.parseLong(key.substring(key.indexOf('|') + 1))); }

	private static int power(MinecraftServer server, String key) {
		ServerLevel l = levelOf(server, key);
		return l == null ? 0 : AxleNetwork.scan(l, posOf(key)).available() + (BOOSTED.contains(key) ? LightningRodBlock.BOOST : 0);
	}

	private static void deliver(MinecraftServer server, MachineJobs.Finished f) {
		BOOSTED.remove(f.key());
		ServerLevel l = levelOf(server, f.key());
		if (l == null) return;
		Identifier id = Identifier.tryParse(f.recipe().output());
		var item = id == null ? java.util.Optional.<net.minecraft.world.item.Item>empty() : BuiltInRegistries.ITEM.getOptional(id);
		if (item.isPresent()) Block.popResource(l, posOf(f.key()), new ItemStack(item.get(), f.recipe().outputCount()));
		if (WEAR.computeIfAbsent(f.key(), x -> new MachineWear(true)).onProduct(l.getRandom().nextDouble())) {
			l.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, posOf(f.key()).getX() + 0.5, posOf(f.key()).getY() + 1.0, posOf(f.key()).getZ() + 0.5, 8, 0.2, 0.2, 0.2, 0.02);
		}
	}

	/** Herstelt de slijtage van een machine (moersleutel); false als er niets mis was. */
	public static boolean repair(ServerLevel level, BlockPos pos) {
		MachineWear w = WEAR.get(key(level, pos));
		return w != null && w.repair();
	}

	/** Alleen voor tests: zet de machine op deze plek direct op slijtage-stand "kapot". */
	public static void breakForTest(ServerLevel level, BlockPos pos) {
		MachineWear w = WEAR.computeIfAbsent(key(level, pos), x -> new MachineWear(true));
		for (int i = 0; i < 1000 && !w.broken(); i++) w.onProduct(0.0);
	}

	public static boolean broken(ServerLevel level, BlockPos pos) {
		MachineWear w = WEAR.get(key(level, pos));
		return w != null && w.broken();
	}

	/** Klik op een machine: toont de voortgang, of start een nieuw werk met de spullen uit de inventaris. */
	public static String click(Player player, ServerLevel level, BlockPos pos, String machine, int kracht) {
		String k = key(level, pos);
		if (JOBS.running(k)) return "Bezig: " + JOBS.percent(k) + "%";
		MachineWear slijtage = WEAR.get(k);
		if (slijtage != null && slijtage.broken()) return "Storing! Herstel de machine met een moersleutel.";
		Map<String, Integer> have = new HashMap<>();
		var inv = player.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack s = inv.getItem(i);
			if (!s.isEmpty()) have.merge(BuiltInRegistries.ITEM.getKey(s.getItem()).toString(), s.getCount(), Integer::sum);
		}
		var found = ModRecipes.book().forMachine(machine).stream()
			.filter(x -> x.power() <= kracht && x.inputs().entrySet().stream().allMatch(e -> have.getOrDefault(e.getKey(), 0) >= e.getValue()))
			.findFirst();
		boolean boost = false;
		if (found.isEmpty()) {
			// Te weinig kracht? Een geladen bliksemafleider in de buurt geeft voor één taak extra kracht.
			int extra = kracht + LightningRodBlock.BOOST;
			var met = ModRecipes.book().forMachine(machine).stream()
				.filter(x -> x.power() <= extra && x.inputs().entrySet().stream().allMatch(e -> have.getOrDefault(e.getKey(), 0) >= e.getValue()))
				.findFirst();
			if (met.isPresent() && LightningRodBlock.drawNear(level, pos)) {
				found = met;
				boost = true;
			}
		}
		if (found.isEmpty()) return com.drakenrijken.core.MachineStatus.whyNot(ModRecipes.book().forMachine(machine), have, kracht);
		MachineRecipe r = found.get();
		if (!JOBS.start(k, r)) return "Te veel machines tegelijk aan het werk.";
		if (boost) BOOSTED.add(k);
		for (var e : r.inputs().entrySet()) MachineBlock.remove(player, e.getKey(), e.getValue());
		return "Aan het werk: " + r.output() + " (" + (r.ticks() / 20) + " s)";
	}

	/** Alleen voor tests: loopt een werk op deze plek? */
	public static boolean running(ServerLevel level, BlockPos pos) { return JOBS.running(key(level, pos)); }

	/** Alleen voor tests: zet alles af en geeft alles terug. */
	public static void clearForTest() { JOBS.finishAll(); WEAR.clear(); BOOSTED.clear(); }
}
