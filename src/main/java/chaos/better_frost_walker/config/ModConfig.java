package chaos.better_frost_walker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static chaos.better_frost_walker.BetterFrostWalkerMain.LOGGER;
import static chaos.better_frost_walker.BetterFrostWalkerMain.MOD_ID;

public class ModConfig {
    private static final Path CONFIG_FILE_LOCATION = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID).resolve("%s.json".formatted(MOD_ID));
    private static final File CONFIG_FILE = CONFIG_FILE_LOCATION.toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int maxLevel;

    public boolean generateIceWhileStill;

    public boolean generateIceWhileStillAfterRestart;

    public boolean standingOnPowderedSnow;

    public boolean serverSideOnly;

    public boolean serverSideOnlyAfterRestart;

    public boolean noIceFallDamage;

    public boolean meltIceInTheDark;
    public ModConfig(int maxLevel, boolean generateIceWhileStill, boolean standingOnPowderedSnow, boolean serverSideOnly, boolean noIceFallDamage, boolean meltIceOnTheDark) {
        this.maxLevel = maxLevel;
        this.generateIceWhileStill = generateIceWhileStill;
        this.generateIceWhileStillAfterRestart = generateIceWhileStill;
        this.standingOnPowderedSnow = standingOnPowderedSnow;
        this.serverSideOnly = serverSideOnly;
        this.serverSideOnlyAfterRestart = serverSideOnly;
        this.noIceFallDamage = noIceFallDamage;
        this.meltIceInTheDark = meltIceOnTheDark;
    }
    public static ModConfig defaultConfig() {
        return new ModConfig(4, true, true, false, true, true);
    }
    public static ModConfig createOrLoad() {
        if (CONFIG_FILE.exists()) {
            try (FileReader fileReader = new FileReader(CONFIG_FILE)) {
                ModConfig config = GSON.fromJson(fileReader, ModConfig.class);
                config.generateIceWhileStill = config.generateIceWhileStillAfterRestart;
                config.serverSideOnly = config.serverSideOnlyAfterRestart;
                return config;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ModConfig config = ModConfig.defaultConfig();
        config.saveToFile();
        return config;
    }

    public void saveToFile() {
        String json = GSON.toJson(this);
        if (!CONFIG_FILE.getParentFile().exists()) {
            if (CONFIG_FILE.getParentFile().mkdirs()) {
                LOGGER.info("Directory created successfully on first time launch");
            } else {
                LOGGER.error("Failed to create directory.");
                return; // Exit the method if directory creation fails
            }
        }

        try (FileWriter fileWriter = new FileWriter(CONFIG_FILE)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
