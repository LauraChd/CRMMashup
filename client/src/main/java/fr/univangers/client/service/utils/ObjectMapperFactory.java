package fr.univangers.client.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory pour obtenir une instance unique de ObjectMapper
 */
public class ObjectMapperFactory {
    private static ObjectMapper mapper = new ObjectMapper();

    private ObjectMapperFactory() {
    }

    /**
     * Retourne l'instance unique de ObjectMapper
     *
     * @return L'instance de ObjectMapper
     */
    public static ObjectMapper instance() {
        return mapper;
    }

}