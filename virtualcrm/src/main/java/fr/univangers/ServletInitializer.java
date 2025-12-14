package fr.univangers;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Initialiseur de servlet pour l'application Spring Boot
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configure l'application
     *
     * @param application Le constructeur de l'application
     * @return Le constructeur configuré
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VirtualCRM.class);
    }

}
