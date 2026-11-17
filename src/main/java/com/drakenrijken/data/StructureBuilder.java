package com.drakenrijken.data;

import com.drakenrijken.core.Blueprint;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

/** Zet een bouwplan uit de core in de wereld. Onbekende blokken worden overgeslagen. */
public final class StructureBuilder {
	private StructureBuilder() {}

	/** Plaatst het plan met de hoek (0,0,0) op {@code corner}; geeft het aantal geplaatste blokken. */
	public static int place(ServerLevel level, BlockPos corner, Blueprint plan) {
		int n = 0;
		for (var p : plan.placements()) {
			var id = Identifier.tryParse(p.block());
			var block = id == null ? null : BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
			if (block == null) continue;
			level.setBlock(corner.offset(p.x(), p.y(), p.z()), block.defaultBlockState(), 2);
			n++;
		}
		return n;
	}
}
