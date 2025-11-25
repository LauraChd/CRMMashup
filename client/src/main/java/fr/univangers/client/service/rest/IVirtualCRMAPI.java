package fr.univangers.client.service.rest;

import fr.univangers.client.service.exceptions.InvalidParametersException;
import fr.univangers.client.service.exceptions.LeadNotFoundException;

public interface IVirtualCRMAPI {

    String addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) throws InvalidParametersException;
    String findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws LeadNotFoundException, InvalidParametersException;
    String findLeadsByDate(String startDate, String endDate) throws InvalidParametersException;
    String getLeadById(String id) throws LeadNotFoundException;
    String getLeads() ;
    String countLeads() ;
    String deleteLead(String id) throws LeadNotFoundException, InvalidParametersException;

}

