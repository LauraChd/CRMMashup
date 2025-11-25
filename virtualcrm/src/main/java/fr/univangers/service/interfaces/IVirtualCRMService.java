package fr.univangers.service.interfaces;

import fr.univangers.internalcrm.thrift.*;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

/**
 * TODO
 */
public interface IVirtualCRMService {

    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException, IOException, Exception;

    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException, IOException, Exception;

    public VirtualLeadDto getLeadById(String ID) throws LeadNotFoundException, TException, IOException;

    public boolean deleteLead(String ID) throws LeadNotFoundException, TException, IOException, Exception;

    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException, Exception;

    public List<VirtualLeadDto> getLeads() throws TException, IOException;

    public int countLeads() throws TException, IOException;
}
