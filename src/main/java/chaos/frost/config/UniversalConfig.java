package chaos.frost.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static com.mojang.text2speech.Narrator.LOGGER;

public class UniversalConfig {
    private static final Path CONFIG_FILE_LOCATION = FabricLoader.getInstance().getConfigDir().resolve("better-frost-walker").resolve("better-frost-walker.json");
    private static final File CONFIG_FILE = CONFIG_FILE_LOCATION.toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int maxLevel;

    public boolean generateIceWhileStill;

    public boolean standingOnPowderedSnow;

    public boolean serverSideOnly;

    public boolean serverSideOnlyAfterRestart;

    public boolean noIceFallDamage;

    public boolean meltIceInTheDark;
    public UniversalConfig(int maxLevel, boolean generateIceWhileStill, boolean standingOnPowderedSnow, boolean serverSideOnly, boolean noIceFallDamage, boolean meltIceOnTheDark) {
        this.maxLevel = maxLevel;
        this.generateIceWhileStill = generateIceWhileStill;
        this.standingOnPowderedSnow = standingOnPowderedSnow;
        this.serverSideOnly = serverSideOnly;
        this.serverSideOnlyAfterRestart = serverSideOnly;
        this.noIceFallDamage = noIceFallDamage;
        this.meltIceInTheDark = meltIceOnTheDark;
    }
    public static UniversalConfig defaultConfig() {
        return new UniversalConfig(4, true, true, false, true, true);
    }
    public static UniversalConfig createOrLoad() {
        if (CONFIG_FILE.exists()) {
            try (FileReader fileReader = new FileReader(CONFIG_FILE)) {
                UniversalConfig config = GSON.fromJson(fileReader, UniversalConfig.class);
                config.serverSideOnly = config.serverSideOnlyAfterRestart;
                return config;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        UniversalConfig config = UniversalConfig.defaultConfig();
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
