package fr.univangers.client;

import org.apache.thrift.TException;
import fr.univangers.client.service.dto.LeadDto;
import fr.univangers.client.service.exceptions.LeadNotFoundException;

import java.util.List;

/**
 * Interface définissant les services accessibles au client
 */
public interface IClientService {

    /**
     * Ajoute un lead
     *
     * @param fullName      Nom complet du lead
     * @param annualRevenue Revenu annuel
     * @param phone         Numéro de téléphone
     * @param street        Rue
     * @param postalCode    Code postal
     * @param city          Ville
     * @param country       Pays
     * @param company       Entreprise
     * @param state         État
     * @return L'ID du lead ajouté
     */
    String addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city,
            String country, String company, String state);

    /**
     * Trouve des leads par revenu annuel
     *
     * @param lowAnnualRevenue  Revenu minimum
     * @param highAnnualRevenue Revenu maximum
     * @param state             État
     * @return Liste des leads trouvés
     * @throws TException En cas d'erreur Thrift
     */
    List<LeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws TException;

    /**
     * Trouve des leads par date
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Liste des leads trouvés
     * @throws TException En cas d'erreur Thrift
     */
    List<LeadDto> findLeadsByDate(long startDate, long endDate) throws TException;

    /**
     * Récupère un lead par son ID
     *
     * @param id ID du lead
     * @return Le lead trouvé
     * @throws LeadNotFoundException Si le lead n'est pas trouvé
     * @throws TException            En cas d'erreur Thrift
     */
    LeadDto getLeadById(String id) throws LeadNotFoundException, TException;

    /**
     * Récupère tous les leads
     *
     * @return Liste de tous les leads
     * @throws TException En cas d'erreur Thrift
     */
    List<LeadDto> getLeads() throws TException;

    /**
     * Compte le nombre de leads
     *
     * @return Le nombre de leads
     * @throws TException En cas d'erreur Thrift
     */
    int countLeads() throws TException;

    /**
     * Supprime un lead par son ID
     *
     * @param id ID du lead à supprimer
     * @throws TException En cas d'erreur Thrift
     */
    void deleteLead(String id) throws TException;

}
