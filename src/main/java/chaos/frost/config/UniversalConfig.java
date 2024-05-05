package chaos.frost.config;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UniversalConfig {

    public int maxLevel;

    public boolean generateIceWhileStill;

    public boolean standingOnPowderedSnow;

    public boolean serverSideOnly;
    public UniversalConfig(int maxLevel, boolean generateIceWhileStill, boolean standingOnPowderedSnow, boolean serverSideOnly) {
        this.maxLevel = maxLevel;
        this.generateIceWhileStill = generateIceWhileStill;
        this.standingOnPowderedSnow = standingOnPowderedSnow;
        this.serverSideOnly = serverSideOnly;
    }
    public static UniversalConfig defaultConfig() {
        return new UniversalConfig(4, true, true, false);
    }
    public static UniversalConfig createAndLoad() {
        if (new File("config/universal-config/universal-config.json").exists()) {
            Gson gson = new Gson();
            try (FileReader fileReader = new FileReader("config/universal-config/universal-config.json")) {
                return gson.fromJson(fileReader, UniversalConfig.class);
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
        if (!new File("config/universal-config").exists()) {
            if (new File("config/universal-config").mkdirs()) {
                System.out.println("Directory created successfully on first time launch");
            } else {
                System.out.println("Failed to create directory.");
                return; // Exit the method if directory creation fails
            }
        }

        try (FileWriter fileWriter = new FileWriter("config/universal-config/universal-config.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
