package org.example.client;

import org.apache.thrift.TException;
import org.example.client.service.dto.LeadDto;
import org.example.client.service.exceptions.LeadNotFoundException;

import java.util.List;

public interface IClientService {

    String addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state);
    List<LeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws TException;
    List<LeadDto> findLeadsByDate(long startDate, long endDate) throws TException;
    LeadDto getLeadById(String id) throws LeadNotFoundException, TException;
    List<LeadDto> getLeads() throws TException;
    int countLeads() throws TException;
    void deleteLead(String id) throws TException;

}
