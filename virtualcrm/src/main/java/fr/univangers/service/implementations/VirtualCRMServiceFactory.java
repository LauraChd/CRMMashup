package fr.univangers.service.implementations;

import fr.univangers.clients.GeoLocalisationServiceClient;
import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;

import java.io.IOException;

public class VirtualCRMServiceFactory {
    private static VirtualCRMServiceImpl virtualCRMService;

    private VirtualCRMServiceFactory() {}

    public synchronized static VirtualCRMServiceImpl getInstance() throws IOException {
        if (virtualCRMService == null) {
            virtualCRMService = new VirtualCRMServiceImpl(new SalesforceCRMClient(), new InternalCRMClient(), new GeoLocalisationServiceClient());
        }
        return virtualCRMService;
    }
}
