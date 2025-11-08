package fr.univangers.service.interfaces;

import fr.univangers.thrift.*;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;

import java.util.List;

/**
 * TODO
 */
public interface ICRMServices {

    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException;

    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException;

    public VirtualLeadDto getLeadById(int ID) throws LeadNotFoundException, TException;

    public void deleteLead(int ID) throws LeadNotFoundException, TException;

    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException;

    public List<VirtualLeadDto> getLeads() throws TException;

    public int countLeads() throws TException;
}
