package fr.univangers.client;

import fr.univangers.client.service.exceptions.ForbiddenActionException;
import fr.univangers.client.service.exceptions.LeadNotFoundException;
import fr.univangers.client.service.rest.VirtualCRMAPI;
import fr.univangers.client.service.utils.exceptions.InputValidationException;

/**
 * TODO
 */
public class Main {

    private static final VirtualCRMAPI virtualCRMAPIService = new VirtualCRMAPI();

    public static void main(String[] args) throws ForbiddenActionException, InputValidationException, LeadNotFoundException {

        String res = "";
        try {
            if (args.length == 0 || args[0].equals("help")) {
                res = getHelp("Usage:\n");
            }
            // ADD
            if (args[0].equals("addLead")) {
                System.out.println("Adding lead...");
                System.out.println("Lead added, here is his id :");
                if(args.length < 10) getHelp("Oops, you seems to need some help :\n");
                else res = virtualCRMAPIService.addLead(args[1], Double.parseDouble(args[2]), args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
            }
            // DELETE
            else if (args[0].equals("deleteLead")) {
                System.out.println("Deleting lead...");
                if(args.length < 2) getHelp("Oops, you seems to need some help :\n");
                else {
                    try {
                        virtualCRMAPIService.deleteLead(args[1]);
                        res = "Deletion successful";
                    } catch (LeadNotFoundException e) {
                        res = e.getMessage();
                    }
                }
            }
            // FIND BY REVENUE
            else if (args[0].equals("findLeads")) {
                System.out.println("Finding leads...");
                System.out.println("Leads found by revenue : ");
                if(args.length < 4) getHelp("Oops, you seems to need some help :\n");
                else res = virtualCRMAPIService.findLeads(Double.parseDouble(args[1]), Double.parseDouble(args[2]), args[3]);
            }
            // GET LEAD BY ID
            else if (args[0].equals("getLeadById")) {
                System.out.println("Getting lead...");
                System.out.println("Lead of id " + args[1] + " :");
                if(args.length < 2) getHelp("Oops, you seems to need some help :\n");
                else res = virtualCRMAPIService.getLeadById(args[1]);
            }
            // GET ALL LEADS
            else if (args[0].equals("getLeads")) {
                System.out.println("Getting leads...");
                System.out.println("Leads : ");
                res = virtualCRMAPIService.getLeads();
            }
            // COUNT
            else if (args[0].equals("countLeads")) {
                System.out.println("Counting leads...");
                System.out.println("Total number of leads : ");
                res = virtualCRMAPIService.countLeads();
            }
            // FIND LEADS BY DATE
            else if (args[0].equals("findLeadsByDate")) {
                System.out.println("Finding leads by date...");
                System.out.println("Leads found by date : ");
                if(args.length < 3) getHelp("Oops, you seems to need some help :\n");
                res = virtualCRMAPIService.findLeadsByDate(args[1], args[2]);
            }
            // MERGE SALESFORCE AND INTERNALCRM
            else if (args[0].equals("merge")) {
                System.out.println("Merging leads from salesforce and internalCRM...");
                res = virtualCRMAPIService.merge();
            }
            System.out.println(res);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    private static String getHelp(String intro) {
        String res;
        res = intro +
                "    [add]    addLead <fullName (formated like \"first, last\")> <annualRevenue> <phone> <company> <street> <postalCode> <city> <country> <state>  \n" +
                "    [remove] deleteLead <leadId>\n" +
                "    [find by revenue]   findLeads <lowAnnualRevenue> <highAnnualRevenue> <state>\n" +
                "    [find by date]   findLeadsByDate <startDate> <endDate> \n" +
                "    [get one]    getLeadById <leadId>\n" +
                "    [get all]    getLeads\n" +
                "    [count]    countLeads\n";

        System.out.println(res);
        return res;
    }

}