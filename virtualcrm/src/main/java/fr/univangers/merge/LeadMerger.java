package fr.univangers.merge;


import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.model.VirtualLeadDto;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

public class LeadMerger {

    private final SalesforceCRMClient salesforceClient;
    private final InternalCRMClient internalClient;

    public LeadMerger() throws IOException {
        this.salesforceClient = new SalesforceCRMClient();
        this.internalClient  = new InternalCRMClient();
    }

    public void merge() throws TException, IOException {

        List<VirtualLeadDto> sfLeads      = salesforceClient.getLeads();
        List<VirtualLeadDto> internalLeads = internalClient.getLeads();

        int added = 0;
        int skipped = 0;

        for (VirtualLeadDto sfLead : sfLeads) {
            if (isDuplicate(sfLead, internalLeads)) {
                skipped++;
                continue;
            }

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
                    sfLead.getState()
            );

            added++;
        }

        System.out.println("Merge terminé. Ajoutés = " + added + ", ignorés (doublons) = " + skipped);
    }

    private boolean isDuplicate(VirtualLeadDto a, List<VirtualLeadDto> list) {
        return list.stream().anyMatch(l ->
                safeEq(l.getFirstName(), a.getFirstName()) &&
                        safeEq(l.getLastName(), a.getLastName()) &&
                        safeEq(l.getCompany(), a.getCompany()) &&
                        safeEq(l.getPhone(), a.getPhone()) &&
                        safeEq(l.getStreet(), a.getStreet()) &&
                        safeEq(l.getCity(), a.getCity()) &&
                        safeEq(l.getCountry(), a.getCountry()) &&
                        safeEq(l.getState(), a.getState())
        );
    }

    private boolean safeEq(String a, String b) {
        if (a == null) return b == null;
        return a.equals(b);
    }
}
