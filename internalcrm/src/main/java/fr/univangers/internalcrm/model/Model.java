package fr.univangers.internalcrm.model;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.List;

/**
 * TODO
 */
public interface Model {

    public List<ILead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, java.lang.String state) throws InvalidRevenueRangeException, TException;

    public List<ILead> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException;

    public void deleteLead(ILead lead) throws LeadNotFoundException, TException;

    public void addLead(ILead lead) throws LeadNotFoundException, LeadAlreadyExistsException, InvalidLeadParameterException, TException;

}
