package chaos.frost.client;

import chaos.frost.keybinds.ModKeybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.sound.SoundEvents;

public class NewFrostwalkerClient implements ClientModInitializer {
    public static boolean isFrostWalkerEnabled = true;

    @Override
    public void onInitializeClient() {
        ModKeybinds.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ModKeybinds.TOGGLE_FROST_WALKER.wasPressed() && client.player != null) {
                isFrostWalkerEnabled = !isFrostWalkerEnabled;
                client.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.2f, isFrostWalkerEnabled ? 1.5f : 0.5f);

                //MinecraftClient.getInstance().getToastManager().add(new ToggleFrostWalkerToast());
            }
        });
    }
}
