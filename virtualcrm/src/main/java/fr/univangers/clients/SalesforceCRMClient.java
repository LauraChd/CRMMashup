package fr.univangers.clients;

import fr.univangers.model.VirtualLeadDto;
import org.example.internalcrm.thrift.*;
import org.apache.thrift.TException;

import java.util.List;

/**
 * TODO
 */
public class SalesforceCRMClient implements CRMClient {


    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException {
        return List.of(); //TODO
    }

    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {
        return List.of(); //TODO
    }

    public VirtualLeadDto getLeadById(int id) throws LeadNotFoundException, TException {
        return null;
    }

    public List<VirtualLeadDto> getLeads() throws TException {
        return List.of();
    }

    public int countLeads() throws TException {
        return 0;
    }

    public void deleteLead(int id) throws LeadNotFoundException, TException {

    }
}
