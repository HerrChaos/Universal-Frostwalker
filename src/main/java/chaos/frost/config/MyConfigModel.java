package chaos.frost.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.RestartRequired;

@Modmenu(modId = "frost")
@Config(name = "frost-config", wrapperName = "MyConfig")
public class MyConfigModel {
    @RestartRequired
    @RangeConstraint(min=1, max= 250)
    public int MaxLevel = 4;

    public boolean generateIceWhileStill = true;
}