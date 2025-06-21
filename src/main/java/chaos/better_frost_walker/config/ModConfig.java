package chaos.better_frost_walker.config;

import blue.endless.jankson.Comment;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import top.offsetmonkey538.offsetconfig538.api.config.Config;
import top.offsetmonkey538.offsetconfig538.api.config.Datafixer;

import java.nio.file.Path;

import static chaos.better_frost_walker.BetterFrostWalkerMain.MOD_ID;

public class ModConfig implements Config {
    @Comment("Whether or not to generate ice while the player is standing still. Default: true")
    public boolean generateIceWhileStill = true;
    @Comment("Whether or not the player is able to stand on powdered snow when wearing frost walker, like when leather boots are worn. Default: true")
    public boolean standingOnPowderedSnow = true;
    @Comment("Whether or not the mod can work without being installed on the client. This will disable lava freezing! Default: false")
    public boolean serverSideOnly = false;
    @Comment("Whether the player doesn't take fall damage from frosted ice/magma when wearing frost walker. Default: true")
    public boolean noIceFallDamage = true;
    @Comment("Whether or not frosted ice/magma melts in dark environments. Default: true")
    public boolean meltIceInTheDark = true;

    @Override
    public @NotNull Datafixer[] getDatafixers() {
        return new Datafixer[] {
                (json, jankson) -> {
                    // 0 -> 1
                    // no-op
                }
        };
    }

    @Override
    public @Range(from = 0L, to = 2147483647L) int getConfigVersion() {
        return 1;
    }

    @Override
    public @NotNull Path getFilePath() {
        return FabricLoader.getInstance().getConfigDir().resolve(MOD_ID).resolve("%s.json".formatted(MOD_ID));
    }
}
