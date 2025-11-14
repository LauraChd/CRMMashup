package org.example.client;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.TException;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

/**
 * TODO
 */
public class Main {

    private static final VirtualCRMAPI virtualCRMAPIService = new VirtualCRMAPI();

    public static void main(String[] args) {

        String res = "";

        if (args.length == 0 || args[0].equals("help")) {
            res = "Usage:\n" +
                    "    [add]    addLead <fullName (formated like \"first, last\")> <annualRevenue> <street> <postalCode> <city> <country> <company> <state>\n" +
                    "    [remove] deleteLead <leadId>\n" +
                    "    [find by revenue]   findLeads <lowAnnualRevenue> <highAnnualRevenue> <state>\n" +
                    "    [find by date]   findLeadsByDate <startDate> <endDate> \n" +
                    "    [get one]    getLeadById <leadId>\n" +
                    "    [get all]    getLeads\n" +
                    "    [count]    countLeads\n";

            System.out.println(res);
        }

        if (args[0].equals("addLeads")) {
            System.out.println("Adding lead...");
            int id = virtualCRMAPIService.addLead(/*TODO*/);
            res = "ID du lead ajouté : " + id;
            System.out.println(res);
        }
        else if (args[0].equals("deleteLead")) {
            System.out.println("Deleting lead...");
            virtualCRMAPIService.deleteLead(/*TODO*/); //TODO : mettre un try catch et si erreur, afficher message "pas de lead avec cet id"
            try{

            } catch(Exception e){
                System.out.println();
            }
        }
        else if (args[0].equals("findLeads")) {
            System.out.println("Finding leads...");
            virtualCRMAPIService.findLeads(/*TODO*/);
        }
        else if (args[0].equals("getLeadById")) {
            System.out.println("Getting lead...");
            virtualCRMAPIService.getLeadById(/*TODO*/);
        }
        else if (args[0].equals("getLeads")) {
            System.out.println("Getting leads...");
            virtualCRMAPIService.getLeads(/*TODO*/);
        }
        else if (args[0].equals("countLeads")) {
            System.out.println("Counting leads...");
            virtualCRMAPIService.countLeads(/*TODO*/);
        }
        else if (args[0].equals("findLeadsByDate")) {
            System.out.println("Finding leads by date...");
            virtualCRMAPIService.findLeadByDate(/*TODO*/);
        }


    }

}