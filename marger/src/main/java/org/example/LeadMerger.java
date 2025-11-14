package org.example;

import fr.univangers.clients.InternalCRMClient;
import fr.univangers.clients.SalesforceCRMClient;
import fr.univangers.model.VirtualLeadDto;
import org.example.internalcrm.thrift.InvalidLeadParameterException;
import org.example.internalcrm.thrift.LeadAlreadyExistsException;

import java.io.IOException;
import java.util.List;

public class LeadMerger {
    private final SalesforceCRMClient salesforce;
    private final InternalCRMClient internal;

    public LeadMerger() throws IOException {
        this.salesforce = new SalesforceCRMClient();
        this.internal   = new InternalCRMClient();
    }

    public void mergeLeads() throws TException, TException {
        List<VirtualLeadDto> sfLeads = salesforce.getLeads();

        int inserted = 0, skipped = 0, errors = 0;

        for (VirtualLeadDto v : sfLeads) {
            String fullName = buildFullName(v); // "Prenom,Nom" comme exige InternalCRM

            try {
                internal.addLead(
                        fullName,
                        safeRevenue(v.getAnnualRevenue()),
                        safeStr(v.getPhone()),
                        safeStr(v.getStreet()),
                        safeStr(v.getPostalCode()),
                        safeStr(v.getCity()),
                        safeStr(v.getCountry()),
                        safeStr(v.getCompany()),
                        safeStr(v.getState())
                );
                inserted++;
                System.out.println("[OK] " + fullName);
            } catch (LeadAlreadyExistsException e) {
                skipped++;
                System.out.println("[SKIP] déjà existant : " + fullName);
            } catch (InvalidLeadParameterException e) {
                errors++;
                System.err.println("[ERR] invalide : " + fullName);
            } catch (TException e) {
                errors++;
                System.err.println("[ERR] Thrift : " + e.getMessage());
            } catch (org.apache.thrift.TException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("Résultat merge: inserted=%d, skipped=%d, errors=%d%n",
                inserted, skipped, errors);
    }

    private String buildFullName(VirtualLeadDto v) {
        String first = safeStr(v.getFirstName());
        String last  = safeStr(v.getLastName());
        return first + "," + last; // format imposé par InternalCRM
    }

    private String safeStr(String s) { return s == null ? "" : s; }
    private double safeRevenue(Double d) { return d == null ? 0.0 : d; }
}

}
