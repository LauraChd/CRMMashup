package fr.univangers.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe de configuration pour charger les propriétés.
 */
public class Config {
    private final Properties props = new Properties();

    /**
     * Constructeur. Charge le fichier de configuration.
     *
     * @param resourceName Nom du fichier de ressource.
     * @throws IOException En cas d'erreur de lecture.
     */
    public Config(String resourceName) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + resourceName);
            }
            props.load(is);
        }
    }

    /**
     * Récupère une propriété par sa clé.
     *
     * @param key La clé de la propriété.
     * @return La valeur de la propriété.
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
