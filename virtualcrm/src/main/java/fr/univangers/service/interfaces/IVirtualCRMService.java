package fr.univangers.service.interfaces;

import fr.univangers.internalcrm.thrift.*;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

/**
 * Interface définissant les opérations du service VirtualCRM
 */
public interface IVirtualCRMService {

        /**
         * Trouve des leads par revenu
         *
         * @param lowAnnualRevenue  Revenu minimum
         * @param highAnnualRevenue Revenu maximum
         * @param state             État
         * @return Liste des leads trouvés
         * @throws InvalidRevenueRangeException Si la plage de revenus est invalide
         * @throws TException                   En cas d'erreur Thrift
         * @throws IOException                  En cas d'erreur d'entrée/sortie
         * @throws Exception                    En cas d'erreur générale
         */
        public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
                        throws InvalidRevenueRangeException, TException, IOException, Exception;

        /**
         * Trouve des leads par date
         *
         * @param startDate Date de début
         * @param endDate   Date de fin
         * @return Liste des leads trouvés
         * @throws InvalidDateException Si la plage de dates est invalide
         * @throws TException           En cas d'erreur Thrift
         * @throws IOException          En cas d'erreur d'entrée/sortie
         * @throws Exception            En cas d'erreur générale
         */
        public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate)
                        throws InvalidDateException, TException, IOException, Exception;

        /**
         * Récupère un lead par son ID
         *
         * @param ID ID du lead
         * @return Le lead trouvé
         * @throws LeadNotFoundException Si le lead n'est pas trouvé
         * @throws TException            En cas d'erreur Thrift
         * @throws IOException           En cas d'erreur d'entrée/sortie
         */
        public VirtualLeadDto getLeadById(String ID) throws LeadNotFoundException, TException, IOException;

        /**
         * Supprime un lead
         *
         * @param ID ID du lead à supprimer
         * @return Vrai si la suppression a réussi
         * @throws LeadNotFoundException Si le lead n'est pas trouvé
         * @throws TException            En cas d'erreur Thrift
         * @throws IOException           En cas d'erreur d'entrée/sortie
         * @throws Exception             En cas d'erreur générale
         */
        public boolean deleteLead(String ID) throws LeadNotFoundException, TException, IOException, Exception;

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
         * @throws LeadAlreadyExistsException    Si le lead existe déjà
         * @throws InvalidLeadParameterException Si les paramètres sont invalides
         * @throws TException                    En cas d'erreur Thrift
         * @throws Exception                     En cas d'erreur générale
         */
        public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode,
                        String city, String country, String company, String state)
                        throws LeadAlreadyExistsException, InvalidLeadParameterException, TException, Exception;

        /**
         * Récupère tous les leads
         *
         * @return Liste de tous les leads
         * @throws TException  En cas d'erreur Thrift
         * @throws IOException En cas d'erreur d'entrée/sortie
         */
        public List<VirtualLeadDto> getLeads() throws TException, IOException;

        /**
         * Compte le nombre de leads
         *
         * @return Le nombre de leads
         * @throws TException  En cas d'erreur Thrift
         * @throws IOException En cas d'erreur d'entrée/sortie
         */
        public int countLeads() throws TException, IOException;

        /**
         * Fusionne les leads
         *
         * @return Résultat de la fusion
         * @throws TException  En cas d'erreur Thrift
         * @throws IOException En cas d'erreur d'entrée/sortie
         */
        String merge() throws TException, IOException;
}
