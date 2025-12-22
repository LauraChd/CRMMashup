package fr.univangers.merge;

import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

/**
 * Classe qui permet de merge les leads kde salesforce et de l'internalcrm
 */
public class LeadMerger {

    private final SalesforceCRMClient salesforceClient;
    private final InternalCRMClient internalClient;

    /**
     * Constructeur. Initialise les clients CRM
     *
     * @throws IOException En cas d'erreur d'initialisation
     */
    public LeadMerger() throws IOException {
        this.salesforceClient = new SalesforceCRMClient();
        this.internalClient = new InternalCRMClient();
    }

    /**
     * Fusionne les leads de Salesforce vers InternalCRM en évitant les doublons
     *
     * @throws TException  En cas d'erreur Thrift
     * @throws IOException En cas d'erreur d'entrée/sortie
     */
    public void merge() throws TException, IOException {

        List<VirtualLeadDto> sfLeads = salesforceClient.getLeads();
        List<VirtualLeadDto> internalLeads = internalClient.getLeads();

        int added = 0;
        int skipped = 0;

        for (VirtualLeadDto sfLead : sfLeads) {
            try {

                String fullName = sfLead.getLastName() + ", " + sfLead.getFirstName();

                internalClient.addLead(
                        fullName,
                        sfLead.getAnnualRevenue(),
                        sfLead.getPhone(),
                        sfLead.getStreet(),
                        sfLead.getPostalCode(),
                        sfLead.getCity(),
                        sfLead.getCountry(),
                        sfLead.getCompany(),
                        sfLead.getState());

                added++;
            } catch (Exception e) {
            }
        }

        System.out.println("Merge terminé. Ajoutés = " + added + ", ignorés (doublons) = " + skipped);
    }
}