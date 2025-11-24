package fr.univangers.internalcrm.service;

import fr.univangers.internalcrm.utils.InternalLeadConverter;
import fr.univangers.internalcrm.model.ILead;
import fr.univangers.internalcrm.model.ModelImpl;
import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;


import java.util.List;

/**
 * TODO
 */
public class InternalCRMServiceImpl implements InternalCRMService.Iface {

    /**
     *
     * @param lowAnnualRevenue minimum annual revenue of the lead
     * @param highAnnualRevenue maximal annual revenue of the lead
     * @param state departement where the lead works
     * @return a list of Leads that meet the criterias given
     * @throws InvalidRevenueRangeException if the minimum annual revenue is higher than maximal annual revenue
     * @throws TException
     */
    @Override
    public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException {
        try {
            List<ILead> leadsModel = ModelImpl.getInstance().findLeads(lowAnnualRevenue, highAnnualRevenue, state);
            return InternalLeadConverter.toInternalLeadDTOList(leadsModel);
        } catch (InvalidRevenueRangeException e) {
            throw new InvalidRevenueRangeException(e.getMessage(), e.getLowAnnualRevenue(), e.getHighAnnualRevenue());
        }
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
    public List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {
        try {
            List<ILead> leadsModel = ModelImpl.getInstance().findLeadsByDate(startDate, endDate);
            return InternalLeadConverter.toInternalLeadDTOList(leadsModel);
        } catch (InvalidDateException e) {
            throw new InvalidDateException(e.getMessage(), e.getStartDate(), e.getEndDate());

        }
    }

    /**
     * TODO
     * @param ID
     * @return
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public InternalLeadDto getLeadById(int ID) throws LeadNotFoundException, TException {
        try {
            ILead lead = ModelImpl.getInstance().getLeadByID(ID);
            return InternalLeadConverter.toInternalLeadDTO(lead);
        } catch (LeadNotFoundException e) {
            throw new LeadNotFoundException("Le lead n'existe pas", ID);
        }
    }

    /**
     * TODO
     * @param ID
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public void deleteLead(int ID) throws LeadNotFoundException, TException {
        try {
            ModelImpl.getInstance().deleteLead(ID);
        } catch (LeadNotFoundException e) {
            throw new LeadNotFoundException(e.getMessage(), e.getID());

        }
    }

    /**
     * TODO
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
        try {
            return ModelImpl.getInstance().addLead(fullName, annualRevenue, phone, street, postalCode, city, country, company, state);
        } catch (LeadAlreadyExistsException e) {
            throw new LeadAlreadyExistsException(e.getMessage(), e.getID());
        } catch (InvalidLeadParameterException ee) {
            throw new InvalidLeadParameterException(ee.getMessage());
        }
    }

    /**
     * TODO
     * @return
     */
    @Override
    public List<InternalLeadDto> getLeads() {
        try {
            return InternalLeadConverter.toInternalLeadDTOList(ModelImpl.getInstance().getAllLeads());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO
     * @return
     */
    @Override
    public int countLeads() {
        try {
            return ModelImpl.getInstance().countLeads();
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}
