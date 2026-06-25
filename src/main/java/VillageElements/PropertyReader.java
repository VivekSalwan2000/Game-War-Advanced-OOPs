package VillageElements;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * Loads the {@code levels.properties} game-balance configuration from the classpath.
 *
 * <p>The file is read once and cached, since it is immutable at runtime and was
 * previously re-read from disk on every entity construction.</p>
 */
public class PropertyReader implements Serializable {

    private static final String RESOURCE_NAME = "levels.properties";
    private static volatile Properties cachedProperties;

    /**
     * Returns the game-balance properties, loading them from the classpath on first call.
     *
     * @return the (cached) properties parsed from {@code levels.properties}
     * @throws IllegalStateException if the resource is missing or cannot be parsed
     */
    public static Properties readPropertiesFile() {
        Properties result = cachedProperties;
        if (result == null) {
            synchronized (PropertyReader.class) {
                result = cachedProperties;
                if (result == null) {
                    result = load();
                    cachedProperties = result;
                }
            }
        }
        return result;
    }

    private static Properties load() {
        Properties prop = new Properties();
        try (InputStream is = PropertyReader.class.getClassLoader().getResourceAsStream(RESOURCE_NAME)) {
            if (is == null) {
                throw new IllegalStateException("Required resource '" + RESOURCE_NAME + "' not found on classpath");
            }
            prop.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load '" + RESOURCE_NAME + "'", e);
        }
        return prop;
    }
}
