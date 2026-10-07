package org.kaddicus.nocreativedriftsimplified.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

public class NoCreativeDriftSimplifiedConfig {
    public static boolean disableVerticalDrift;
    public static final boolean DEFAULT_DISABLE_VERTICAL_DRIFT = false;
    private static final String DISABLE_VERTICAL_DRIFT_KEY = "disableVerticalDrift";

    public static boolean disableNonCreativeDrift;
    public static final boolean DEFAULT_DISABLE_NON_CREATIVE_DRIFT = false;
    private static final String DISABLE_NON_CREATIVE_DRIFT_KEY = "disableNonCreativeDrift";

    public static int driftStrength; // 0=VANILLA, 1=STRONG, 2=WEAK, 3=DISABLED
    public static final int DEFAULT_DRIFT_STRENGTH = 3;
    private static final String DRIFT_STRENGTH_KEY = "driftStrength";

    public static boolean confirmation;
    public static final boolean DEFAULT_CONFIRMATION = true;
    private static final String CONFIRMATION_KEY = "confirmation";

    public static boolean confirmationType;
    public static final boolean DEFAULT_CONFIRMATION_TYPE = true; // true = overlay, false = system
    private static final String CONFIRMATION_TYPE_KEY = "confirmationType";

    public static void save() {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve("nocreativedrift-simplified.properties").toFile();

        try (Writer writer = new FileWriter(configFile)) {
            Properties properties = new Properties();
            properties.setProperty(DISABLE_VERTICAL_DRIFT_KEY, Boolean.toString(disableVerticalDrift));
            properties.setProperty(DISABLE_NON_CREATIVE_DRIFT_KEY, Boolean.toString(disableNonCreativeDrift));
            properties.setProperty(DRIFT_STRENGTH_KEY, Integer.toString(driftStrength));
            properties.setProperty(CONFIRMATION_KEY, Boolean.toString(confirmation));
            properties.setProperty(CONFIRMATION_TYPE_KEY, Boolean.toString(confirmationType));
            properties.store(writer, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve("nocreativedrift-simplified.properties").toFile();
        if (!configFile.exists()) {
            try (Writer writer = new FileWriter(configFile)) {
                Properties properties = new Properties();
                properties.setProperty(DISABLE_VERTICAL_DRIFT_KEY, Boolean.toString(DEFAULT_DISABLE_VERTICAL_DRIFT));
                properties.setProperty(DISABLE_NON_CREATIVE_DRIFT_KEY, Boolean.toString(DEFAULT_DISABLE_NON_CREATIVE_DRIFT));
                properties.setProperty(DRIFT_STRENGTH_KEY, Integer.toString(DEFAULT_DRIFT_STRENGTH));
                properties.setProperty(CONFIRMATION_KEY, Boolean.toString(DEFAULT_CONFIRMATION));
                properties.setProperty(CONFIRMATION_TYPE_KEY, Boolean.toString(DEFAULT_CONFIRMATION_TYPE));
                properties.store(writer, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (Reader reader = new FileReader(configFile)) {
            Properties properties = new Properties();
            properties.load(reader);
            disableVerticalDrift = Boolean.parseBoolean(properties.getProperty(DISABLE_VERTICAL_DRIFT_KEY, String.valueOf(DEFAULT_DISABLE_VERTICAL_DRIFT)));
            disableNonCreativeDrift = Boolean.parseBoolean(properties.getProperty(DISABLE_NON_CREATIVE_DRIFT_KEY, String.valueOf(DEFAULT_DISABLE_NON_CREATIVE_DRIFT)));
            driftStrength = Integer.parseInt(properties.getProperty(DRIFT_STRENGTH_KEY, String.valueOf(DEFAULT_DRIFT_STRENGTH)));
            confirmation = Boolean.parseBoolean(properties.getProperty(CONFIRMATION_KEY, String.valueOf(DEFAULT_CONFIRMATION)));
            confirmationType = Boolean.parseBoolean(properties.getProperty(CONFIRMATION_TYPE_KEY, String.valueOf(DEFAULT_CONFIRMATION_TYPE)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            save();
        }
    }
}