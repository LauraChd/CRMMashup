package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.List;

public class ModelImpl {
    static {
        List<LeadTo> leadsModel;
    }


    public static List<LeadTo> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException{
        return null;
    }

    public static List<LeadTo> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException{
        return null;
    }

    public static void deleteLead(LeadTo lead) throws LeadNotFoundException, TException{

    }

    public static void addLead(LeadTo lead) throws LeadDoesNotExistException, LeadAlreadyExistsException, InvalidLeadParameterException, TException{

    }

}
