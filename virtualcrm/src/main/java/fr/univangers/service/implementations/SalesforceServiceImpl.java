package fr.univangers.service.implementations;

import fr.univangers.model.VirtualLeadDto;
import fr.univangers.service.interfaces.ICRMServices;
import fr.univangers.thrift.*;
import org.apache.thrift.TException;

import java.util.List;

/**
 * TODO
 */
public class SalesforceServiceImpl implements ICRMServices {

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
        return List.of(); //TODO
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
        return List.of(); //TODO
    }

    /**
     * TODO
     * @param ID
     * @return
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public VirtualLeadDto getLeadById(int ID) throws LeadNotFoundException, TException {
        return null; //TODO
    }

    /**
     * TODO
     * @param ID
     * @throws LeadNotFoundException
     * @throws TException
     */
    @Override
    public void deleteLead(int ID) throws LeadNotFoundException, TException {
        //TODO
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
        return 0; //TODO
    }

    /**
     * TODO
     * @return
     * @throws TException
     */
    @Override
    public List<VirtualLeadDto> getLeads() throws TException {
        return List.of(); //TODO
    }

    /**
     * TODO
     * @return
     * @throws TException
     */
    @Override
    public int countLeads() throws TException {
        return 0; //TODO
    }
}
