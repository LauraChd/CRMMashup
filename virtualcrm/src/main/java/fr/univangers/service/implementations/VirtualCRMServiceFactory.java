package fr.univangers.service.implementations;

import fr.univangers.clients.GeoLocalisationServiceClient;
import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.service.interfaces.VirtualCRMService;

import java.io.IOException;

public class VirtualCRMServiceFactory {
    private static VirtualCRMService virtualCRMService;

    private VirtualCRMServiceFactory() {}

    public synchronized static VirtualCRMService getInstance() throws IOException {
        if (virtualCRMService == null) {
            virtualCRMService = new VirtualCRMServiceImpl(new SalesforceCRMClient(), new InternalCRMClient(), new GeoLocalisationServiceClient());
        }
        return virtualCRMService;
    }
}
