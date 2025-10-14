package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl {

    // Singleton
    private static ModelImpl instance = null;

    // Leads list ~= Database
    private final List<ILead> iLeads;

    // Counts the number of leads and gives their IDs
    private int leadsCount = 1 ;

    // Private constructor because it's a singleton
    private ModelImpl() {
        this.iLeads = new ArrayList<>();
    }

    // Global access to the singleton
    public static synchronized ModelImpl getInstance() {
        if (instance == null) {
            instance = new ModelImpl();
        }
        return instance;
    }

    // Find leads by revenue and state

    /**
     *
     * @param lowAnnualRevenue
     * @param highAnnualRevenue
     * @param state in the company (can be an empty string (= ""))
     * @return the list of the leads that matches the requirements
     * @throws InvalidRevenueRangeException if lowAnnualRevenue is higher than highannualRevenue
     * @throws TException
     */

    public List<ILead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws InvalidRevenueRangeException, TException {

        if (lowAnnualRevenue > highAnnualRevenue) {
            throw new InvalidRevenueRangeException("Invalid revenue range", lowAnnualRevenue, highAnnualRevenue);
        }

        List<ILead> leadsResult = new ArrayList<>();
        for (ILead l : iLeads) {
            if (l.getAnnualRevenue() >= lowAnnualRevenue &&
                    l.getAnnualRevenue() <= highAnnualRevenue &&
                    l.getState().equalsIgnoreCase(state)) {

                leadsResult.add(l);
            }
        }
        return leadsResult;
    }

    // Find leads added between two dates

    /**
     *
     * @param startDate minimum date when the lead was added
     * @param endDate maximum date when the lead was added
     * @return the list of the leads that matches the requirements
     * @throws InvalidDateException if the startDate is higher than the endDate
     * @throws TException
     */
    public List<ILead> findLeadsByDate(long startDate, long endDate)
            throws InvalidDateException, TException {
        //TODO : comment client fait pour saisir les dates ? Doit prendre dates format 10/10/2025 et convertir dans Virtual ou internal ?

        if(startDate > endDate) {
            throw new InvalidDateException("Invalid date range", startDate, endDate);
        }

        List<ILead> leadsResult = new ArrayList<>();
        for (ILead l : iLeads) {
            if (l.getCreationDate() >= startDate && l.getCreationDate() <= endDate) {
                leadsResult.add(l);
            }
        }
        return leadsResult;
    }

    // Add a lead to the list of leads

    /**
     *
     * @param fullName as "LastName, FirstName"
     * @param annualRevenue
     * @param phone
     * @param street
     * @param postalCode
     * @param city
     * @param country
     * @param company
     * @param state in the company
     * @return -1 if the creation of lead turned wrong, else the ID of the lead
     * @throws LeadAlreadyExistsException if a lead already exists with the same parameters
     * @throws InvalidLeadParameterException if a parameter is not of the right type
     * @throws TException
     */
    public int addLead(String fullName, double annualRevenue, String phone,
            String street, String postalCode, String city, String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {

        // Splits the fullName in both
        String[] splitFullName = Utils.splitFullName(fullName);
        // Get the number of the lead
        int ID = leadsCount;
        // Create a Lead with the parameters
        ILead newLead = new ILead(ID, splitFullName[0], splitFullName[1], annualRevenue, phone, street,
                postalCode, city, country, System.currentTimeMillis(), company, state);
        // Check is this lead already exists
        for (ILead iLead : iLeads) {
            if (iLead.sameAs(newLead)){
                throw new LeadAlreadyExistsException("Le lead existe déjà !", iLead.getID());
            }
        }
        // If the lead does not already exists, can add it to the list
        leadsCount++;
        iLeads.add(newLead);
        return ID;
    }

    // Delete a lead from ID

    /**
     *
     * @param ID of the lead to delete
     * @throws LeadNotFoundException if the given ID isn't attributed to a lead
     * @throws TException
     */
    public void deleteLead(int ID)
            throws LeadNotFoundException, TException {

        boolean removed = iLeads.removeIf(l -> l.getID() == ID);
        if (!removed) {
            throw new LeadNotFoundException("Le lead n'existe pas", ID);
        }
    }

    // Get all the leads from the list

    /**
     *
     * @return the list of the leads
     */
    public List<ILead> getAllLeads() {
        // Returns a copy
        return new ArrayList<>(iLeads);
    }
}


