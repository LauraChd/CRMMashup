package fr.univangers.service.implementations;

import fr.univangers.clients.GeoLocalisationServiceClient;
import fr.univangers.clients.InternalICRMClient;
import fr.univangers.clients.SalesforceICRMClient;

import java.io.IOException;

public class VirtualCRMServiceFactory {
    private static VirtualCRMServiceImpl virtualCRMService;

    private VirtualCRMServiceFactory() {}

    public synchronized static VirtualCRMServiceImpl getInstance() throws IOException {
        if (virtualCRMService == null) {
            virtualCRMService = new VirtualCRMServiceImpl(new SalesforceICRMClient(), new InternalICRMClient(), new GeoLocalisationServiceClient());
        }
        return virtualCRMService;
    }
}
