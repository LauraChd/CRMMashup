package fr.univangers.service.interfaces;

import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.io.IOException;
import java.util.List;

/**
 * TODO
 */
public interface VirtualCRMService {

    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException, IOException;

    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException, IOException;

    public VirtualLeadDto getLeadById(String ID) throws LeadNotFoundException, TException, IOException;

    public boolean deleteLead(String ID) throws LeadNotFoundException, TException, IOException;

    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException;

    public List<VirtualLeadDto> getLeads() throws TException, IOException;

    public int countLeads() throws TException;
}
