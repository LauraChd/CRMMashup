package fr.univangers.client;

import java.lang.reflect.InvocationTargetException;

public class ClientServiceFactory {

    private final static String SERVICE_CLASSNAME = "org.example.client.service.rest.VirtualCRMAPI";
    private static Class<IClientService> serviceClass = null;

    private ClientServiceFactory() {
    }

    /**
     * Constructeur privé pour créer un singleton
     */
    @SuppressWarnings("unchecked")
    private synchronized static Class<IClientService> getServiceClass() {

        if (serviceClass == null) {
            try {
                serviceClass = (Class<IClientService>) Class.forName(SERVICE_CLASSNAME);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static IClientService getService() {

        try {
            return (IClientService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
