package chaos.frost.networking.custom;

import chaos.frost.NewFrostwalker;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ToggleFrostwalkerC2SPacket(boolean enabled) implements CustomPayload {
    public static final Identifier SUMMON_LIGHTNING_PAYLOAD_ID = NewFrostwalker.id("summon_lightning");
    public static final CustomPayload.Id<ToggleFrostwalkerC2SPacket> ID = new CustomPayload.Id<>(SUMMON_LIGHTNING_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ToggleFrostwalkerC2SPacket> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN, ToggleFrostwalkerC2SPacket::enabled, ToggleFrostwalkerC2SPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
