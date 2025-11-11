package fr.univangers.service.implementations;

import fr.univangers.clients.GeoLocalisationServiceClient;
import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.model.GeographicPointDto;
import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.interfaces.VirtualCRMService;
import org.example.internalcrm.thrift.*;
import fr.univangers.utils.VirtualLeadConverter;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VirtualCRMServiceImpl implements VirtualCRMService {

    // TODO
    private final SalesforceCRMClient salesforceCRMClient = new SalesforceCRMClient();
    //TODO
    private final InternalCRMClient internalCRMClient = new InternalCRMClient();
    //TODO
    private final GeoLocalisationServiceClient geoLocalisationServiceClient = new GeoLocalisationServiceClient();


    /**
     * TODO
     * @param lowAnnualRevenue
     * @param highAnnualRevenue
     * @param state
     * @return
     * @throws InvalidRevenueRangeException
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException, IOException {
        List<VirtualLeadDto> leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                salesforceCRMClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state),
                internalCRMClient.findLeads(lowAnnualRevenue, highAnnualRevenue, state)
        );

        if (leadsList != null) {
            for(VirtualLeadDto lead : leadsList)
            {
                Optional<GeographicPointDto> geographicPoint = geoLocalisationServiceClient.lookup(lead);
                geographicPoint.ifPresent(lead::setGeographicPointDto);
            }
        }

        return leadsList;
    }

    /**
     * TODO
     * @param startDate
     * @param endDate
     * @return
     * @throws InvalidDateException
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException, IOException {
        List<VirtualLeadDto> leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                salesforceCRMClient.findLeadsByDate(startDate, endDate),
                internalCRMClient.findLeadsByDate(startDate, endDate)
        );

        if (leadsList != null) {
            for(VirtualLeadDto lead : leadsList)
            {
                Optional<GeographicPointDto> geographicPoint = geoLocalisationServiceClient.lookup(lead);
                geographicPoint.ifPresent(lead::setGeographicPointDto);
            }
        }

        return leadsList;
    }

    /**
     * TODO
     * @param id
     * @return
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public VirtualLeadDto getLeadById(int id) throws LeadNotFoundException, TException {
        VirtualLeadDto leadInternal = internalCRMClient.getLeadById(id);
        VirtualLeadDto leadSalesforce = salesforceCRMClient.getLeadById(id);

        //TODO : fonction pour prendre le seul lead qui existe

        //TODO : set location to virtualLeadDto
        return null;
    }

    /**
     * TODO
     *
     * @param id
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public void deleteLead(int id) throws LeadNotFoundException, TException {

        //TODO : try catch
        // si le premier marche pas, faire celui d'apres, sinon lancer exception
        internalCRMClient.deleteLead(id);
        salesforceCRMClient.deleteLead(id);

    }

    /**
     * TODO
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

        //TODO : ajouter dans lequel ?
        return 0;
    }

    /**
     * TODO
     *
     * @return
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> getLeads() throws TException {
        List<VirtualLeadDto> leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                salesforceCRMClient.getLeads(),
                internalCRMClient.getLeads()
        );

        //TODO : set localisation du virtualLeadDto
        return leadsList;
    }

    /**
     * TODO
     *
     * @return
     * @throws TException
     */
    @Override
    public int countLeads() throws TException {
        int countLeads = salesforceCRMClient.countLeads() + internalCRMClient.countLeads();
        return countLeads;
    }
}
