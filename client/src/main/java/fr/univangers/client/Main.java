package fr.univangers.client;

import fr.univangers.client.service.exceptions.LeadNotFoundException;
import fr.univangers.client.service.rest.VirtualCRMAPI;

/**
 * Point d’entrée principal de l’application en ligne de commande.
 * Cette classe permet d’appeler les services via les arguments CLI.
 */
public class Main {

    private static final VirtualCRMAPI virtualCRMAPIService = new VirtualCRMAPI();

    /**
     * Méthode principale.
     *
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {

        String res = "";
        try {
            // affichage de l'aide
            if (args.length == 0 || args[0].equals("help")) {
                res = "Usage:\n" +
                        "    [add]    addLead <fullName (formated like \"first, last\")> <annualRevenue> <street> <postalCode> <city> <country> <company> <state>\n"
                        +
                        "    [remove] deleteLead <leadId>\n" +
                        "    [find by revenue]   findLeads <lowAnnualRevenue> <highAnnualRevenue> <state>\n" +
                        "    [find by date]   findLeadsByDate <startDate> <endDate> \n" +
                        "    [get one]    getLeadById <leadId>\n" +
                        "    [get all]    getLeads\n" +
                        "    [count]    countLeads\n";

                System.out.println(res);
            }
            // ajout d’un lead
            if (args[0].equals("addLead")) {
                System.out.println("Adding lead...");
                System.out.println("Lead added, here is his id :");
                res = virtualCRMAPIService.addLead(args[1], Double.parseDouble(args[2]), args[3], args[4], args[5],
                        args[6], args[7], args[8], args[9]);
            }
            // suppression d’un lead
            else if (args[0].equals("deleteLead")) {
                System.out.println("Deleting lead...");
                try {
                    virtualCRMAPIService.deleteLead(args[1]);
                    res = "Deletion successful";
                } catch (LeadNotFoundException e) {
                    res = e.getMessage();
                }
            }
            // recherche par revenus
            else if (args[0].equals("findLeads")) {
                System.out.println("Finding leads...");
                System.out.println("Leads found by revenue : ");
                res = virtualCRMAPIService.findLeads(Double.parseDouble(args[1]), Double.parseDouble(args[2]), args[3]);
            }
            // récupération d'un lead par id
            else if (args[0].equals("getLeadById")) {
                System.out.println("Getting lead...");
                System.out.println("Lead of id " + args[1] + " :");
                res = virtualCRMAPIService.getLeadById(args[1]);
            }
            // récupère tous les leads
            else if (args[0].equals("getLeads")) {
                System.out.println("Getting leads...");
                System.out.println("Leads : ");
                res = virtualCRMAPIService.getLeads();
            }
            // compte tous les leads
            else if (args[0].equals("countLeads")) {
                System.out.println("Counting leads...");
                System.out.println("Total number of leads : ");
                res = virtualCRMAPIService.countLeads();
            }
            // recherche par date
            else if (args[0].equals("findLeadsByDate")) {
                System.out.println("Finding leads by date...");
                System.out.println("Leads found by date : ");
                res = virtualCRMAPIService.findLeadsByDate(args[1], args[2]);
            }
            // fusion Salesforce / InternalCRM
            else if (args[0].equals("merge")) {
                System.out.println("Merging leads from salesforce and internalCRM...");
                res = virtualCRMAPIService.merge();
            }
            System.out.println(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}