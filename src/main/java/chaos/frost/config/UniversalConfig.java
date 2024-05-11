package chaos.frost.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class UniversalConfig {
    private static final Path CONFIG_FILE_LOCATION = FabricLoader.getInstance().getConfigDir().resolve("better-frost-walker").resolve("better-frost-walker.json");
    private static final File CONFIG_FILE = CONFIG_FILE_LOCATION.toFile();

    public int maxLevel;

    public boolean generateIceWhileStill;

    public boolean standingOnPowderedSnow;

    public boolean serverSideOnly;

    public boolean serverSideOnlyAfterRestart;
    public boolean noIceFallDamage;
    public UniversalConfig(int maxLevel, boolean generateIceWhileStill, boolean standingOnPowderedSnow, boolean serverSideOnly, boolean noIceFallDamage) {
        this.maxLevel = maxLevel;
        this.generateIceWhileStill = generateIceWhileStill;
        this.standingOnPowderedSnow = standingOnPowderedSnow;
        this.serverSideOnly = serverSideOnly;
        this.serverSideOnlyAfterRestart = serverSideOnly;
        this.noIceFallDamage = noIceFallDamage;
    }
    public static UniversalConfig defaultConfig() {
        return new UniversalConfig(4, true, true, false, true);
    }
    public static UniversalConfig createOrLoad() {
        if (CONFIG_FILE.exists()) {
            Gson gson = new Gson();
            try (FileReader fileReader = new FileReader(CONFIG_FILE)) {
                UniversalConfig config = gson.fromJson(fileReader, UniversalConfig.class);
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
        Gson gson = new Gson();
        String json = gson.toJson(this);
        if (!CONFIG_FILE.getParentFile().exists()) {
            if (CONFIG_FILE.getParentFile().mkdirs()) {
                System.out.println("Directory created successfully on first time launch");
            } else {
                System.out.println("Failed to create directory.");
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
