package fr.univangers.clients;

import fr.univangers.config.Config;
import fr.univangers.model.VirtualLeadDto;
import org.example.internalcrm.thrift.*;
import org.apache.thrift.TException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class SalesforceCRMClient implements CRMClient {


    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private final String instanceUrl;
    private String accessToken;

    public SalesforceCRMClient() throws IOException {
        Config config = new Config("config.properties");
        this.clientId = config.getProperty("CLIENT_ID");
        this.clientSecret = config.getProperty("CLIENT_SECRET");
        this.username = config.getProperty("USERNAME");
        this.password = config.getProperty("PASSWORD");
        this.instanceUrl = config.getProperty("INSTANCE_URL");
    }


    /**
     * Authentification à Salesforce et récupération de l'access_token
     */
    private void authenticate() throws IOException {
        String urlStr = "https://login.salesforce.com/services/oauth2/token";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Corps de la requête
        String body = "grant_type=password"
                + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        // Lire la réponse JSON
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String response = reader.lines().reduce("", (acc, line) -> acc + line);
            JSONObject json = new JSONObject(response);

            this.accessToken = json.getString("access_token");
        }
    }

    private void ensureAuthenticated() throws IOException {
        if (accessToken == null) {
            authenticate();
        }
    }

    private List<VirtualLeadDto> parseLeads(JSONArray records) {
        List<VirtualLeadDto> leads = new ArrayList<>();
        for (int i = 0; i < records.length(); i++) {
            JSONObject rec = records.getJSONObject(i);
            VirtualLeadDto dto = new VirtualLeadDto();

            dto.setFirstName(rec.optString("FirstName"));
            dto.setLastName(rec.optString("LastName"));
            dto.setCompany(rec.optString("Company"));
            dto.setAnnualRevenue(rec.optDouble("AnnualRevenue"));
            dto.setPhone(rec.optString("Phone"));
            dto.setStreet(rec.optString("Street"));
            dto.setPostalCode(rec.optString("PostalCode"));
            dto.setCity(rec.optString("City"));
            dto.setCountry(rec.optString("Country"));
            String createdDateStr = rec.optString("CreatedDate");
            if (createdDateStr != null && !createdDateStr.isBlank()) {
                // Salesforce renvoie un format ISO complet : 2024-11-11T13:49:00.000+0000
                // Donc il faut utiliser un DateTimeFormatter adapté
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                dto.setCreationDate(LocalDate.parse(createdDateStr, formatter));
            } else {
                dto.setCreationDate(null); // ou garder une valeur par défaut
            }

            leads.add(dto);
        }
        return leads;
    }

    public List<VirtualLeadDto> executeQuery(String soql) throws IOException {
        ensureAuthenticated();
        String urlStr = instanceUrl + "/services/data/v57.0/query?q=" +
                URLEncoder.encode(soql, StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        // Si le token est expiré
        if (responseCode == 401) {
            authenticate();
            conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String response = reader.lines().reduce("", (acc, line) -> acc + line);
            JSONObject json = new JSONObject(response);
            JSONArray records = json.getJSONArray("records");
            return parseLeads(records);
        }
    }



    @Override
    public List<VirtualLeadDto> findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) throws InvalidRevenueRangeException, TException {
        try {
            if (lowAnnualRevenue > highAnnualRevenue) {
                throw new InvalidRevenueRangeException();
            }
            String soql = "SELECT Id, FirstName, LastName, Company, AnnualRevenue, Phone, Street, PostalCode, City, Country, CreatedDate, State "
                    + "FROM Lead WHERE AnnualRevenue >= " + lowAnnualRevenue
                    + " AND AnnualRevenue <= " + highAnnualRevenue
                    + " AND State = '" + state + "'";
            return executeQuery(soql);
        } catch (IOException e) {
            throw new TException(e);
        }
    }

    @Override
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate) throws InvalidDateException, TException {
        return List.of(); //TODO
    }

    @Override
    public VirtualLeadDto getLeadById(int id) throws LeadNotFoundException, TException {
        return null;
    }

    @Override
    public List<VirtualLeadDto> getLeads() throws TException {
        return List.of();
    }

    @Override
    public int countLeads() throws TException {
        return 0;
    }

    public void deleteLead(int id) throws LeadNotFoundException, TException {

    }
}
