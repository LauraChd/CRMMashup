package fr.univangers.client.service.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe qui permet de charger et lire un fichier de configuration
 * au format .properties depuis les ressources du projet.
 */
public class Config {

    // contient toutes les propriétés chargées depuis le fichier
    private final Properties props = new Properties();

    /**
     * Charge un fichier de configuration depuis les ressources.
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
     * Récupère la valeur associée à une clé dans le fichier de configuration.
     *
     * @param key Clé de la propriété.
     * @return La valeur de la propriété.
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }

}
