package com.drakenrijken.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Server naar client: "punten over;id1,id2" voor het vaardighedenscherm. */
public record SkillsPayload(String text) implements CustomPacketPayload {
	public static final Type<SkillsPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("drakenrijken", "skills"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SkillsPayload> CODEC =
		StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SkillsPayload::text, SkillsPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
