package fr.univangers.clients;

import fr.univangers.internalcrm.thrift.*;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;

import java.util.List;

/**
 * Interface définissant les opérations d'un client CRM.
 *
 * @param <ID> Type de l'identifiant du lead.
 */
public interface CRMClient<ID> {
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
    List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws InvalidRevenueRangeException, TException;

    /**
     * Trouve des leads par date.
     *
     * @param startDate Date de début.
     * @param endDate   Date de fin.
     * @return Liste des leads trouvés.
     * @throws InvalidDateException Si la plage de dates est invalide.
     * @throws TException           En cas d'erreur Thrift.
     */
    List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException;

    /**
     * Récupère un lead par son ID.
     *
     * @param id ID du lead.
     * @return Le lead trouvé.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
     */
    VirtualLeadDto getLeadById(ID id) throws LeadNotFoundException, TException;

    /**
     * Récupère tous les leads.
     *
     * @return Liste de tous les leads.
     * @throws TException En cas d'erreur Thrift.
     */
    List<VirtualLeadDto> getLeads() throws TException;

    /**
     * Compte le nombre de leads.
     *
     * @return Le nombre de leads.
     * @throws TException En cas d'erreur Thrift.
     */
    int countLeads() throws TException;

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
    ID addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city,
            String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException;

    /**
     * Supprime un lead.
     *
     * @param id ID du lead à supprimer.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
     */
    void deleteLead(ID id) throws LeadNotFoundException, TException;
}
