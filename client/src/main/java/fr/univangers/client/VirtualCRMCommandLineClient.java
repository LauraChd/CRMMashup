package fr.univangers.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class VirtualCRMCommandLineClient {

    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        System.out.println("=== VirtualCRM Console Client ===");
        System.out.println("Tapez 'help' pour voir les commandes, 'exit' pour quitter.");

        while (true) {

            System.out.print("\n> ");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Au revoir !");
                break;
            }

            if (line.equalsIgnoreCase("help")) {
                printUsage();
                continue;
            }

            // Découper la ligne en mots
            String[] cmd = line.split(" ");

            if (cmd.length == 0) continue;

            String command = cmd[0];

            switch (command) {

                case "findLeads":
                    if(cmd.length != 4) {
                        System.out.println("Usage: findLeads low high state");
                        break;
                    }
                    findLeads(cmd[1], cmd[2], cmd[3]);
                    break;

                case "findLeadsByDate":
                    if(cmd.length != 3) {
                        System.out.println("Usage: findLeadsByDate startDate endDate");
                        break;
                    }
                    findLeadsByDate(cmd[1], cmd[2]);
                    break;

                case "getLead":
                    if(cmd.length != 2) {
                        System.out.println("Usage: getLead id");
                        break;
                    }
                    getLeadById(cmd[1]);
                    break;

                case "deleteLead":
                    if(cmd.length != 2) {
                        System.out.println("Usage: deleteLead id");
                        break;
                    }
                    deleteLead(cmd[1]);
                    break;

                case "addLead":
                    if(cmd.length != 10) {
                        System.out.println("Usage: addLead fullName revenue phone street postalCode city country company state");
                        break;
                    }
                    addLead(cmd);
                    break;

                case "getLeads":
                    getLeads();
                    break;

                case "countLeads":
                    countLeads();
                    break;

                default:
                    System.out.println("Commande inconnue. Tapez 'help'.");
            }
        }

        sc.close();
    }


    // HTTP GET
    private static String httpGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        return readResponse(con);
    }


    // HTTP DELETE
    private static String httpDelete(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

        return readResponse(con);
    }

    // HTTP POST (form-data urlencoded)
    private static String httpPost(String urlStr, String params) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try(OutputStream os = con.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        return readResponse(con);
    }


    // Read HTTP response
    private static String readResponse(HttpURLConnection con) throws IOException {
        int status = con.getResponseCode();
        BufferedReader in;

        if(status >= 200 && status < 300) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder content = new StringBuilder();

        while((inputLine = in.readLine()) != null) {
            content.append(inputLine).append("\n");
        }

        in.close();
        return content.toString();
    }


    // COMMAND IMPLEMENTATIONS


    private static void findLeads(String low, String high, String state) throws Exception {
        String url = BASE_URL + "/findLeads?lowAnnualRevenue=" + low +
                "&highAnnualRevenue=" + high +
                "&state=" + state;
        System.out.println(httpGet(url));
    }

    private static void findLeadsByDate(String start, String end) throws Exception {
        String url = BASE_URL + "/findLeadsByDate?startDate=" + start +
                "&endDate=" + end;
        System.out.println(httpGet(url));
    }

    private static void getLeadById(String id) throws Exception {
        String url = BASE_URL + "/lead/" + id;
        System.out.println(httpGet(url));
    }

    private static void deleteLead(String id) throws Exception {
        String url = BASE_URL + "/lead/" + id;
        System.out.println(httpDelete(url));
    }

    private static void addLead(String[] args) throws Exception {
        String params =
                "fullName=" + args[1] +
                        "&annualRevenue=" + args[2] +
                        "&phone=" + args[3] +
                        "&street=" + args[4] +
                        "&postalCode=" + args[5] +
                        "&city=" + args[6] +
                        "&country=" + args[7] +
                        "&company=" + args[8] +
                        "&state=" + args[9];

        System.out.println(httpPost(BASE_URL + "/lead", params));
    }

    private static void getLeads() throws Exception {
        System.out.println(httpGet(BASE_URL + "/leads"));
    }

    private static void countLeads() throws Exception {
        System.out.println(httpGet(BASE_URL + "/countLeads"));
    }


    // HELP
    private static void printUsage() {
        System.out.println("Commandes disponibles :");
        System.out.println("  findLeads low high state");
        System.out.println("  findLeadsByDate startDate endDate");
        System.out.println("  getLead id");
        System.out.println("  deleteLead id");
        System.out.println("  addLead fullName revenue phone street postalCode city country company state");
        System.out.println("  getLeads");
        System.out.println("  countLeads");
    }
}
