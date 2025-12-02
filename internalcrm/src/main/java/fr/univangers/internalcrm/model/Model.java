package fr.univangers.internalcrm.model;

import fr.univangers.internalcrm.thrift.*;
import org.apache.thrift.TException;

import java.util.List;

/**
 * Interface définissant les opérations du modèle.
 */
public interface Model {

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
    public List<ILead> findLeads(double lowAnnualRevenue, double highAnnualRevenue, java.lang.String state)
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
    public List<ILead> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException;

    /**
     * Supprime un lead.
     *
     * @param lead Le lead à supprimer.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     * @throws TException            En cas d'erreur Thrift.
     */
    public void deleteLead(ILead lead) throws LeadNotFoundException, TException;

    /**
     * Ajoute un lead.
     *
     * @param lead Le lead à ajouter.
     * @throws LeadNotFoundException         Si le lead n'est pas trouvé.
     * @throws LeadAlreadyExistsException    Si le lead existe déjà.
     * @throws InvalidLeadParameterException Si les paramètres sont invalides.
     * @throws TException                    En cas d'erreur Thrift.
     */
    public void addLead(ILead lead)
            throws LeadNotFoundException, LeadAlreadyExistsException, InvalidLeadParameterException, TException;

}
