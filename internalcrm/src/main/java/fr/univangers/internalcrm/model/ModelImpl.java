package fr.univangers.internalcrm.model;

import fr.univangers.internalcrm.thrift.*;
import fr.univangers.internalcrm.utils.InternalLeadConverter;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrètes des opérations
 */
public class ModelImpl {

    // Singleton
    private static ModelImpl instance = null;

    // Leads list ~= Database
    private final List<ILead> iLeads;

    // compte le nombre de leads et donne leurs IDs
    private int leadsCount = 1;

    private ModelImpl() throws TException {
        this.iLeads = new ArrayList<>();

        int lead1 = addLead("Dupont, Alice", 120000.50, "+33123456789",
                "12 rue de la Paix", "75002", "Paris", "France", "Dupont SA", "Île-de-France");

        int lead2 = addLead("Martin, Bob", 950000.00, "+447912345678",
                "221B Baker Street", "NW1 6XE", "London", "UK", "Martin & Co Ltd", "Greater London");

        int lead3 = addLead("Bernard, Chloé", 35000.00, "+33698765432",
                "5 avenue des Champs", "33000", "Bordeaux", "France", "Chloé Consulting", "Nouvelle-Aquitaine");

        int lead4 = addLead("Smith, David", 500000.00, "+14165551234",
                "500 Market St", "94105", "San Francisco", "USA", "Smith Corp", "California");

        int lead5 = addLead("Johnson, Emma", 78000.00, "+61234567890",
                "10 George St", "2000", "Sydney", "Australia", "Emma Ventures", "New South Wales");
    }

    /**
     * Global access to the singleton
     * 
     * @return the unique instance of the model
     */
    public static synchronized ModelImpl getInstance() throws TException {
        if (instance == null) {
            instance = new ModelImpl();
        }
        return instance;
    }

    /**
     * Find leads by revenue and state
     * 
     * @param lowAnnualRevenue  minimum annual revenue of the lead
     * @param highAnnualRevenue maximal annual revenue of the lead
     * @param state             departement where the lead works
     * @return a list of Leads that meet the criterias given
     * @throws InvalidRevenueRangeException if the minimum annual revenue is higher
     *                                      than maximal annual revenue
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
                    l.getState() != null &&
                    l.getState().equalsIgnoreCase(state)) {

                leadsResult.add(l);
            }
        }
        return leadsResult;
    }

    /**
     * Find leads added between two dates
     * 
     * @param startDate minimum date when the lead was added
     * @param endDate   maximum date when the lead was added
     * @return the list of the leads that matches the requirements
     * @throws InvalidDateException if the startDate is higher than the endDate
     * @throws TException
     */
    public List<ILead> findLeadsByDate(long startDate, long endDate)
            throws InvalidDateException, TException {
        // TODO : comment client fait pour saisir les dates ? Doit prendre dates format
        // 10/10/2025 et convertir dans Virtual ou internal ?

        if (startDate > endDate) {
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

    /**
     * Add a lead to the list of leads
     * 
     * @param fullName      as "LastName, FirstName"
     * @param annualRevenue
     * @param phone
     * @param street
     * @param postalCode
     * @param city
     * @param country
     * @param company
     * @param state         in the company
     * @return -1 if the creation of lead turned wrong, else the ID of the lead
     * @throws LeadAlreadyExistsException    if a lead already exists with the same
     *                                       parameters
     * @throws InvalidLeadParameterException if a parameter is not of the right type
     * @throws TException
     */
    public int addLead(String fullName, double annualRevenue, String phone,
            String street, String postalCode, String city, String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {

        // Splits the fullName in both
        String[] splitFullName = InternalLeadConverter.splitFullName(fullName);
        // Get the number of the lead
        int ID = leadsCount;
        // Create a Lead with the parameters
        ILead newLead = new ILead(ID, splitFullName[0], splitFullName[1], annualRevenue, phone, street,
                postalCode, city, country, System.currentTimeMillis(), company, state);
        // Check is this lead already exists
        for (ILead iLead : iLeads) {
            if (iLead.sameAs(newLead)) {
                throw new LeadAlreadyExistsException("Le lead existe déjà !", iLead.getID());
            }
        }
        // If the lead does not already exists, can add it to the list
        leadsCount++;
        iLeads.add(newLead);
        return ID;
    }

    /**
     * Delete a lead from ID
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

    /**
     * Get all the leads from the list
     * 
     * @return the list of the leads
     */
    public List<ILead> getAllLeads() {
        // Returns a copy
        return new ArrayList<>(iLeads);
    }

    /**
     * Récupère un lead par son ID
     *
     * @param id ID du lead
     * @return Le lead trouvé
     * @throws LeadNotFoundException Si le lead n'est pas trouvé
     * @throws TException            En cas d'erreur Thrift
     */
    public ILead getLeadByID(int id)
            throws LeadNotFoundException, TException {
        for (ILead lead : iLeads) {
            if (lead.getID() == id) {
                return lead;
            }
        }
        throw new LeadNotFoundException("Le lead n'existe pas", id);
    }

    /**
     * Compte le nombre de leads
     *
     * @return Le nombre de leads
     */
    public int countLeads() {
        return iLeads.size();
    }
}
