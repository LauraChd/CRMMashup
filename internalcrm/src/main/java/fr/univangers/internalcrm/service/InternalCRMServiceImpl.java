package fr.univangers.internalcrm.service;

import fr.univangers.internalcrm.thrift.*;
import fr.univangers.internalcrm.utils.InternalLeadConverter;
import fr.univangers.internalcrm.model.ILead;
import fr.univangers.internalcrm.model.ModelImpl;
import org.apache.thrift.TException;

import java.util.List;

/**
 * Implémentation du service InternalCRM.
 */
public class InternalCRMServiceImpl implements InternalCRMService.Iface {

    /**
     * Trouve des leads par revenu.
     *
     * @param lowAnnualRevenue  Revenu minimum.
     * @param highAnnualRevenue Revenu maximum.
     * @param state             État.
     * @return Liste des leads trouvés.
     * @throws InvalidRevenueRangeException Si la plage de revenus est invalide.
     * @throws TException                   En cas d'erreur Thrift.
     */
    @Override
    public List<InternalLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws InvalidRevenueRangeException, TException {
        try {
            List<ILead> leadsModel = ModelImpl.getInstance().findLeads(lowAnnualRevenue, highAnnualRevenue, state);
            return InternalLeadConverter.toInternalLeadDTOList(leadsModel);
        } catch (InvalidRevenueRangeException e) {
            throw new InvalidRevenueRangeException(e.getMessage(), e.getLowAnnualRevenue(), e.getHighAnnualRevenue());
        }
    }

    /**
     * Trouve des leads par date.
     *
     * @param startDate Date de début.
     * @param endDate   Date de fin.
     * @return Liste des leads trouvés.
     * @throws InvalidDateException Si la plage de dates est invalide.
     * @throws TException           En cas d'erreur Thrift.
     */
    @Override
    public List<InternalLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {
        try {
            List<ILead> leadsModel = ModelImpl.getInstance().findLeadsByDate(startDate, endDate);
            return InternalLeadConverter.toInternalLeadDTOList(leadsModel);
        } catch (InvalidDateException e) {
            throw new InvalidDateException(e.getMessage(), e.getStartDate(), e.getEndDate());

        }
    }

    /**
     * Récupère un lead par son ID.
     *
     * @param ID ID du lead.
     * @return Le lead trouvé.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
     */
    @Override
    public InternalLeadDto getLeadById(int ID) throws LeadNotFoundException, TException {
        try {
            ILead lead = ModelImpl.getInstance().getLeadByID(ID);
            return InternalLeadConverter.toInternalLeadDTO(lead);
        } catch (LeadNotFoundException e) {
            throw new LeadNotFoundException("Le lead n'existe pas", ID);
        }
    }

    /**
     * Supprime un lead.
     *
     * @param ID ID du lead à supprimer.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
     */
    @Override
    public void deleteLead(int ID) throws LeadNotFoundException, TException {
        try {
            ModelImpl.getInstance().deleteLead(ID);
        } catch (LeadNotFoundException e) {
            throw new LeadNotFoundException(e.getMessage(), e.getID());

        }
    }

    /**
     * Ajoute un lead.
     *
     * @param fullName      Nom complet.
     * @param annualRevenue Revenu annuel.
     * @param phone         Téléphone.
     * @param street        Rue.
     * @param postalCode    Code postal.
     * @param city          Ville.
     * @param country       Pays.
     * @param company       Entreprise.
     * @param state         État.
     * @return L'ID du lead ajouté.
     * @throws LeadAlreadyExistsException    Si le lead existe déjà.
     * @throws InvalidLeadParameterException Si les paramètres sont invalides.
     * @throws TException                    En cas d'erreur Thrift.
     */
    @Override
    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode,
            String city, String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {
        try {
            return ModelImpl.getInstance().addLead(fullName, annualRevenue, phone, street, postalCode, city, country,
                    company, state);
        } catch (LeadAlreadyExistsException e) {
            throw new LeadAlreadyExistsException(e.getMessage(), e.getID());
        } catch (InvalidLeadParameterException ee) {
            throw new InvalidLeadParameterException(ee.getMessage());
        }
    }

    /**
     * Récupère tous les leads.
     *
     * @return Liste de tous les leads.
     */
    @Override
    public List<InternalLeadDto> getLeads() {
        try {
            return InternalLeadConverter.toInternalLeadDTOList(ModelImpl.getInstance().getAllLeads());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compte le nombre de leads.
     *
     * @return Le nombre de leads.
     */
    @Override
    public int countLeads() {
        try {
            return ModelImpl.getInstance().countLeads();
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }
}
