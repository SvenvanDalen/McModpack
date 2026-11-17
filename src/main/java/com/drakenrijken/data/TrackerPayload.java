package com.drakenrijken.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Server naar client: de regel voor de quest-tracker (leeg = niets tonen). */
public record TrackerPayload(String text) implements CustomPacketPayload {
	public static final Type<TrackerPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("drakenrijken", "tracker"));
	public static final StreamCodec<RegistryFriendlyByteBuf, TrackerPayload> CODEC =
		StreamCodec.composite(ByteBufCodecs.STRING_UTF8, TrackerPayload::text, TrackerPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
