package fr.univangers.clients;

import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.List;

public interface CRMClient<ID> {
    List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException;
    List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException;
    VirtualLeadDto getLeadById(ID id) throws LeadNotFoundException, TException;
    List<VirtualLeadDto> getLeads() throws TException;
    int countLeads() throws TException;
    int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws LeadAlreadyExistsException, InvalidLeadParameterException, TException;
    void deleteLead(ID id) throws LeadNotFoundException, TException;
}
