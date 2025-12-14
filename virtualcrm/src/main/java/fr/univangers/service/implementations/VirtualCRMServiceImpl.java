package fr.univangers.service.implementations;

import fr.univangers.clients.GeoLocalisationServiceClient;
import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.internalcrm.thrift.*;
import fr.univangers.merge.LeadMerger;
import fr.univangers.model.GeographicPointDto;
import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.interfaces.IVirtualCRMService;
import fr.univangers.utils.VirtualLeadConverter;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Classe principale du VirtualCRM, qui redistribue les requêtes pour met en forme les réponses pour les envoyer au client
 */
public class VirtualCRMServiceImpl implements IVirtualCRMService {

    private final SalesforceCRMClient salesforceCRMClient;
    private final InternalCRMClient internalCRMClient;
    
    private final GeoLocalisationServiceClient geoLocalisationServiceClient;

    // True if a merge has been done
    private boolean merged = false;

    public VirtualCRMServiceImpl(SalesforceCRMClient salesforceCRMClient,
                                 InternalCRMClient internalCRMClient,
                                 GeoLocalisationServiceClient geoLocalisationServiceClient) {
        this.salesforceCRMClient = salesforceCRMClient;
        this.internalCRMClient = internalCRMClient;
        this.geoLocalisationServiceClient = geoLocalisationServiceClient;
    }
 

    /**
     * Recherche les leads qui ont un salaire contenu entre deux valeurs fournies en paramètres
     *
     * @param lowAnnualRevenue
     * @param highAnnualRevenue
     * @param state
     * @return
     * @throws InvalidRevenueRangeException
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException, IOException {
        List<VirtualLeadDto> leadsList = null;
        if(!isMerged()) {
            leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                    salesforceCRMClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state),
                    internalCRMClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state)
            );
        } else {
            leadsList = internalCRMClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state);
        }

        for (VirtualLeadDto lead : leadsList) {
            Optional<GeographicPointDto> geographicPoint = geoLocalisationServiceClient.lookup(lead);
            geographicPoint.ifPresent(lead::setGeographicPointDto);
        }

        return leadsList;
    }

    /**
     * Recherche les leads dont la date de création se situe entre les deux dates fournies en paramètres
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws InvalidDateException
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException, IOException {
        List<VirtualLeadDto> leadsList = null;

        if(!isMerged()){
            leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                    salesforceCRMClient.findLeadsByDate(startDate, endDate),
                    internalCRMClient.findLeadsByDate(startDate, endDate)
            );
        } else {
            leadsList = internalCRMClient.findLeadsByDate(startDate, endDate);
        }

        for (VirtualLeadDto lead : leadsList) {
            Optional<GeographicPointDto> geographicPoint = geoLocalisationServiceClient.lookup(lead);
            geographicPoint.ifPresent(lead::setGeographicPointDto);
        }

        return leadsList;
    }

    /**
     * Récupère et renvoie un lead à partir de son id
     *
     * @param id
     * @return
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public VirtualLeadDto getLeadById(String id) throws LeadNotFoundException, TException, IOException {
        VirtualLeadDto lead = null;

        try {
            // Cas InternalCRM : ID numérique
            int internalId = Integer.parseInt(id);
            lead = internalCRMClient.getLeadById(internalId);
        } catch (NumberFormatException e) {
            // Cas Salesforce : ID non numérique
            if(!isMerged()) {
                lead = salesforceCRMClient.getLeadById(id);
            }
        }

        if (lead == null) {
            throw new LeadNotFoundException();
        }

        // Ajout de la géolocalisation
        if(!lead.getCountry().isEmpty())
        {
            Optional<GeographicPointDto> geo = geoLocalisationServiceClient.lookup(lead);
            geo.ifPresent(lead::setGeographicPointDto);
        }


        return lead;
    }


    /**
     * Supprime un lead à partir de son id, qu'il soit dans Salesforce ou dans l'Internal CRM
     *
     * @param id
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public boolean deleteLead(String id) throws LeadNotFoundException, TException, IOException {

        //TODO : try catch
        // si le premier marche pas, faire celui d'apres, sinon lancer exception

        // Essai InternalCRM
        try {
            int internalId = Integer.parseInt(id);
            internalCRMClient.deleteLead(internalId);
            return true;
        } catch (NumberFormatException e) {
            // Dans le cas où l'ID n'est pas un entier parce que c'est un ID Salesforce
        } catch (LeadNotFoundException e) {
            // pas trouvé dans InternalCRM donc on continue vers Salesforce
        }

        if(!isMerged()) {
            // Essai Salesforce
            try {
                salesforceCRMClient.deleteLead(id);
                return true;
            } catch (LeadNotFoundException e) {
                // pas trouvé dans Salesforce donc on laisse tomber
            }
        }

        // Si aucun des deux n'a réussi
        throw new LeadNotFoundException();
    }

    /**
     * Ajoute un lead dans l'internal CRM
     *
     * @param fullName
     * @param annualRevenue
     * @param phone
     * @param street
     * @param postalCode
     * @param city
     * @param country
     * @param company
     * @param state
     * @return
     * @throws LeadAlreadyExistsException
     * @throws InvalidLeadParameterException
     * @throws TException
     */
    @Override
    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {
        return internalCRMClient.addLead(fullName, annualRevenue, phone, street, postalCode, city, country, company, state);
    }

    /**
     * Renvoie l'ensemble leads, ceux de l'Internal CRM et ceux de Salesforce
     *
     * @return
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> getLeads() throws TException, IOException {
        List<VirtualLeadDto> leadsList = null;
        if(!isMerged()) {
            leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                    salesforceCRMClient.getLeads(),
                    internalCRMClient.getLeads()
            );
        } else {
            leadsList = internalCRMClient.getLeads();
        }

        for (VirtualLeadDto lead : leadsList) {
            if(!lead.getCountry().isEmpty())
            {
                Optional<GeographicPointDto> geographicPoint = geoLocalisationServiceClient.lookup(lead);
                geographicPoint.ifPresent(lead::setGeographicPointDto);
            }
        }

        return leadsList;
    }

    /**
     * Renvoie le nombre cumulé de leads contenus dans les deux CRM
     *
     * @return
     * @throws TException
     */
    @Override
    public int countLeads() throws TException {
        int countLeads = 0;
        if(!isMerged()) {
            countLeads = salesforceCRMClient.countLeads() + internalCRMClient.countLeads();
        } else {
            countLeads = internalCRMClient.countLeads();
        }
        return countLeads;
    }

    @Override
    public String merge() throws TException, IOException {
        LeadMerger merger = new LeadMerger();
        merger.merge();
        setMerged(true);
        return "Merge terminé";
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
}
