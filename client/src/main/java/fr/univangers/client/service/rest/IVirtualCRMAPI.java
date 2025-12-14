package fr.univangers.client.service.rest;

import fr.univangers.client.service.exceptions.InvalidParametersException;
import fr.univangers.client.service.exceptions.LeadNotFoundException;

/**
 * Interface définissant les opérations de l'API VirtualCRM
 */
public interface IVirtualCRMAPI {

    /**
     * Ajoute un lead
     *
     * @param fullName      Nom complet
     * @param annualRevenue Revenu annuel
     * @param phone         Téléphone
     * @param street        Rue
     * @param postalCode    Code postal
     * @param city          Ville
     * @param country       Pays
     * @param company       Entreprise
     * @param state         État
     * @return L'ID du lead ajouté
     * @throws InvalidParametersException Si les paramètres sont invalides
     */
    String addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city,
            String country, String company, String state) throws InvalidParametersException;

    /**
     * Trouve des leads par revenu
     *
     * @param lowAnnualRevenue  Revenu minimum
     * @param highAnnualRevenue Revenu maximum
     * @param state             État
     * @return Liste des leads trouvés
     * @throws LeadNotFoundException      Si aucun lead n'est trouvé
     * @throws InvalidParametersException Si les paramètres sont invalides
     */
    String findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws LeadNotFoundException, InvalidParametersException;

    /**
     * Trouve des leads par date
     *
     * @param startDate Date de début
     * @param endDate   Date de fin
     * @return Liste des leads trouvés
     * @throws InvalidParametersException Si les paramètres sont invalides
     */
    String findLeadsByDate(String startDate, String endDate) throws InvalidParametersException;

    /**
     * Récupère un lead par son ID
     *
     * @param id ID du lead
     * @return Le lead trouvé
     * @throws LeadNotFoundException Si le lead n'est pas trouvé
     */
    String getLeadById(String id) throws LeadNotFoundException;

    /**
     * Récupère tous les leads
     *
     * @return Liste de tous les leads
     */
    String getLeads();

    /**
     * Compte le nombre de leads
     *
     * @return Le nombre de leads
     */
    String countLeads();

    /**
     * Supprime un lead
     *
     * @param id ID du lead à supprimer
     * @return Message de confirmation
     * @throws LeadNotFoundException      Si le lead n'est pas trouvé
     * @throws InvalidParametersException Si les paramètres sont invalides
     */
    String deleteLead(String id) throws LeadNotFoundException, InvalidParametersException;

    /**
     * Fusionne les leads
     *
     * @return Résultat de la fusion
     */
    String merge();
}
