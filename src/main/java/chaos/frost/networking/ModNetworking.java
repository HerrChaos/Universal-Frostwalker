package chaos.frost.networking;

import chaos.frost.data_attachment.ModDataAttachments;
import chaos.frost.networking.custom.ToggleFrostwalkerC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModNetworking {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(ToggleFrostwalkerC2SPacket.ID, ToggleFrostwalkerC2SPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleFrostwalkerC2SPacket.ID, (payload, context) -> {
            context.server().execute(() -> {
                context.player().setAttached(ModDataAttachments.IS_FROSTWALKER_ENABLED, payload.enabled());
            });
        });
    }

    public void cinit() {

    }
}
