package fr.univangers.service.implementations;

import fr.univangers.clients.GeolocationServiceImpl;
import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.interfaces.ICRMServices;
import fr.univangers.thrift.*;
import fr.univangers.utils.VirtualLeadConverter;
import org.apache.thrift.TException;

import java.util.List;

public class VirtualCRMServiceImpl implements ICRMServices {

    // TODO
    private final SalesforceServiceImpl salesforceService = new SalesforceServiceImpl();
    //TODO
    private final InternalCRMServiceImpl internalCRMService = new InternalCRMServiceImpl();
    //TODO
    private final GeolocationServiceImpl geolocationService = new GeolocationServiceImpl();


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
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException {
        List<VirtualLeadDto> leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                salesforceService.findLeads(lowAnnualRevenue, highAnnualRevenue, state),
                internalCRMService.findLeads(lowAnnualRevenue, highAnnualRevenue, state)
        );

        //TODO : set localisation du virtualLeadDto
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
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {
        List<VirtualLeadDto> leadsList = VirtualLeadConverter.mergeInternalSalesforceLeads(
                salesforceService.findLeadsByDate(startDate, endDate),
                internalCRMService.findLeadsByDate(startDate, endDate)
        );

        //TODO : set localisation du virtualLeadDto
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
        VirtualLeadDto leadInternal = internalCRMService.getLeadById(id);
        VirtualLeadDto leadSalesforce = salesforceService.getLeadById(id);

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
        internalCRMService.deleteLead(id);
        salesforceService.deleteLead(id);

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
                salesforceService.getLeads(),
                internalCRMService.getLeads()
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
        int countLeads = salesforceService.countLeads() + internalCRMService.countLeads();
        return countLeads;
    }
}
