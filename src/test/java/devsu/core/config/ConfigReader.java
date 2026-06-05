package devsu.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads and exposes properties from {@code config.properties} on the classpath. */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (stream == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found in config.properties: " + key);
        }
        return value;
    }

    public static int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Property '" + key + "' must be a valid integer, got: '" + value + "'", e);
        }
    }
}
