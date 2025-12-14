package chaos.frost.keybinds;

import chaos.frost.NewFrostwalker;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of(NewFrostwalker.MOD_ID, "keybinds"));
    public static final KeyBinding TOGGLE_FROST_WALKER = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.frost.toggle_frost_walker",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            CATEGORY
    ));

    public static void init() {

    }
}
