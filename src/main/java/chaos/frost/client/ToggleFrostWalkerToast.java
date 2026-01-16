package chaos.frost.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.util.Identifier;

public class ToggleFrostWalkerToast implements Toast {
    private static final Identifier TEXTURE = Identifier.ofVanilla("toast/recipe");


    private Visibility visibility = Visibility.SHOW;

    @Override
    public Visibility getVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager manager, long time) {
        this.visibility = time < 1000.0 ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;

    }

    @Override
    public void draw(DrawContext context, TextRenderer textRenderer, long startTime) {

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, this.getWidth(), this.getHeight());
        context.drawText(textRenderer, "Frost walker is now " + (NewFrostwalkerClient.isFrostWalkerEnabled ? "ON" : "OFF"), 7, 7, rgbaColor(0, 0, 0, 255), false);
    }

    // 255
    public int  rgbaColor(int r, int g, int b, int a) {
        return  ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
