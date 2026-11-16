package com.drakenrijken.test;

import com.drakenrijken.data.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class TransportbandGameTest {
	@GameTest
	public void bandDuwtVoorwerpOost(GameTestHelper context) {
		BlockPos band = new BlockPos(1, 1, 1);
		for (int x = 1; x <= 4; x++) {
			context.setBlock(band.east(x - 1), ModBlocks.TRANSPORTBAND.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
		}
		var level = context.getLevel();
		var abs = context.absolutePos(band);
		var item = new ItemEntity(level, abs.getX() + 0.5, abs.getY() + 1.0, abs.getZ() + 0.5, Items.STICK.getDefaultInstance());
		item.setDeltaMovement(Vec3.ZERO);
		level.addFreshEntity(item);
		double startX = item.getX();
		context.runAfterDelay(15, () -> {
			if (item.getX() <= startX + 0.15) throw new AssertionError("voorwerp is niet verschoven: " + startX + " -> " + item.getX());
			context.succeed();
		});
	}
}
