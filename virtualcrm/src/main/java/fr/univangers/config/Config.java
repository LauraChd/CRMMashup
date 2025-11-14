package fr.univangers.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();

    public Config(String resourceName) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + resourceName);
            }
            props.load(is);
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
