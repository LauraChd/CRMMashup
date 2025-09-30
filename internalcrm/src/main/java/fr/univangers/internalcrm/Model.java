package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.List;

public interface Model {

    public List<LeadTo> findLeads(double lowAnnualRevenue, double highAnnualRevenue, java.lang.String state) throws InvalidRevenueRangeException, TException;

    public List<LeadTo> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException;

    public void deleteLead(LeadTo lead) throws LeadNotFoundException, TException;

    public void addLead(LeadTo lead) throws LeadDoesNotExistException, LeadAlreadyExistsException, InvalidLeadParameterException, TException;

}
