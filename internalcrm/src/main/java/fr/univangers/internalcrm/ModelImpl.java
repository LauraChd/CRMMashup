package fr.univangers.internalcrm;

import org.apache.thrift.TException;
import org.example.internalcrm.thrift.*;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl {

    // Singleton
    private static ModelImpl instance = null;

    //Liste interne des Leads (type interne)
    private final List<ILead> leadsModel;

    //Compteur pour les ID des Leads
    private int leadCpt = 1 ;

    //Constructeur privé
    private ModelImpl() {
        this.leadsModel = new ArrayList<>();
    }

    //Accès global à l’instance unique
    public static synchronized ModelImpl getInstance() {
        if (instance == null) {
            instance = new ModelImpl();
        }
        return instance;
    }

    //Recherche par revenu et état
    public List<ILead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws InvalidRevenueRangeException, TException {

        if (lowAnnualRevenue > highAnnualRevenue) {
            throw new InvalidRevenueRangeException("Invalid revenue range", lowAnnualRevenue, highAnnualRevenue);
        }

        List<ILead> leadsReturn = new ArrayList<>();
        for (ILead l : leadsModel) {
            if (l.getAnnualRevenue() >= lowAnnualRevenue &&
                    l.getAnnualRevenue() <= highAnnualRevenue &&
                    l.getState().equalsIgnoreCase(state)) {

                leadsReturn.add(l);
            }
        }
        return leadsReturn;
    }

    //Recherche par date
    public List<ILead> findLeadsByDate(long startDate, long endDate)
            throws InvalidDateException, TException {
        List<ILead> result = new ArrayList<>();
        // Exemple : à adapter selon ton modèle
        for (ILead l : leadsModel) {
            if (l.getCreationDate() >= startDate && l.getCreationDate() <= endDate) {
                result.add(l);
            }
        }
        return result;
    }

    //Ajouter un lead
    public void addLead(String fullName, double annualRevenue, String phone,
            String street, String postalCode, String city, String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {
        String[] splitFullName = Utils.splitFullName(fullName);

        for (ILead iLead : leadsModel) {
            if (iLead.getAnnualRevenue() == annualRevenue
                    && iLead.getFirstName() != null && iLead.getFirstName().equalsIgnoreCase(splitFullName[0])
                    && iLead.getLastName() != null && iLead.getLastName().equalsIgnoreCase(splitFullName[1])
                    && iLead.getStreet() != null && iLead.getStreet().equalsIgnoreCase(street)
                    && iLead.getPostalCode() != null && iLead.getPostalCode().equalsIgnoreCase(postalCode)
                    && iLead.getCountry() != null && iLead.getCountry().equalsIgnoreCase(country)
                    && iLead.getCompany() != null && iLead.getCompany().equalsIgnoreCase(company)
                    && iLead.getState() != null && iLead.getState().equalsIgnoreCase(state)
            ) {
                throw new LeadAlreadyExistsException("Le lead existe déjà !", null);
            }
        }

        ILead newLead = new ILead(leadCpt, splitFullName[0], splitFullName[1], annualRevenue, phone, street,
                postalCode, city, country, System.currentTimeMillis(), company, state);
        leadsModel.add(newLead);

    }

    //Supprimer un lead
    public void deleteLead(ILead lead)
            throws LeadNotFoundException, TException {
        /*if (!leadsModel.remove(lead)) {
            throw new LeadNotFoundException("Lead not found");
        }*/
        //TODO
    }

    public List<ILead> getAllLeads() {
        return new ArrayList<>(leadsModel);
    }
}


