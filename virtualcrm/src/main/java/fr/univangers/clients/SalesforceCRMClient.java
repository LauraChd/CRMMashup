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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class SalesforceCRMClient implements CRMClient<String> {


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

    private List<VirtualLeadDto> parseLeads(JSONArray leadsJson) {
        List<VirtualLeadDto> leads = new ArrayList<>();
        for (int i = 0; i < leadsJson.length(); i++) {
            JSONObject leadJson = leadsJson.getJSONObject(i);
            VirtualLeadDto virtualLeadDto = new VirtualLeadDto();

            virtualLeadDto.setFirstName(leadJson.optString("FirstName"));
            virtualLeadDto.setLastName(leadJson.optString("LastName"));
            virtualLeadDto.setCompany(leadJson.optString("Company"));
            virtualLeadDto.setAnnualRevenue(leadJson.optDouble("AnnualRevenue"));
            virtualLeadDto.setPhone(leadJson.optString("Phone"));
            virtualLeadDto.setStreet(leadJson.optString("Street"));
            virtualLeadDto.setPostalCode(leadJson.optString("PostalCode"));
            virtualLeadDto.setCity(leadJson.optString("City"));
            virtualLeadDto.setState(leadJson.optString("State"));
            virtualLeadDto.setCountry(leadJson.optString("Country"));
            String createdDateStr = leadJson.optString("CreatedDate");
            if (createdDateStr != null && !createdDateStr.isBlank()) {
                // Conversion du format Salesforce au type LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                virtualLeadDto.setCreationDate(LocalDate.parse(createdDateStr, formatter));
            } else {
                virtualLeadDto.setCreationDate(null);
            }

            leads.add(virtualLeadDto);
        }
        return leads;
    }

    private List<VirtualLeadDto> executeQuery(String soql) throws IOException {
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
            JSONArray responseArray = json.getJSONArray("records");
            return parseLeads(responseArray);
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
    public List<VirtualLeadDto> findLeadsByDate(long startDate, long endDate)
            throws InvalidDateException, TException {
        try {
            if (startDate > endDate) {
                throw new InvalidDateException();
            }

            // Conversion des long (timestamp) en LocalDateTime UTC
            LocalDateTime start = Instant.ofEpochMilli(startDate)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDateTime();
            LocalDateTime end   = Instant.ofEpochMilli(endDate)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDateTime();

            // Conversion des dates au format voulu de SOQL
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String startStr = start.format(formatter);
            String endStr = end.format(formatter);

            // Requête pour Salesforce
            String soql = "SELECT Id, FirstName, LastName, Company, AnnualRevenue, Phone, Street, PostalCode, City, Country, CreatedDate "
                    + "FROM Lead WHERE CreatedDate >= " + startStr
                    + " AND CreatedDate <= " + endStr;

            return executeQuery(soql);
        } catch (IOException e) {
            throw new TException(e);
        }
    }


    public VirtualLeadDto getLeadById(String id) throws LeadNotFoundException, TException {
        try {
            String soql = "SELECT Id, FirstName, LastName, Company, AnnualRevenue, Phone, Street, PostalCode, City, Country, CreatedDate FROM Lead WHERE Id = '" + id + "'";
            List<VirtualLeadDto> leads = executeQuery(soql);
            if (leads.isEmpty()) {
                throw new LeadNotFoundException();
            }
            return leads.get(0);
        } catch (IOException e) {
            throw new TException(e);
        }
    }

    @Override
    public List<VirtualLeadDto> getLeads() throws TException {
        try {
            String soql = "SELECT Id, FirstName, LastName, Company, AnnualRevenue, Phone, Street, PostalCode, City, Country, CreatedDate, State FROM Lead";
            return executeQuery(soql);
        } catch (IOException e) {
            throw new TException(e);
        }
    }

    @Override
    public int countLeads() throws TException {
        try {
            ensureAuthenticated();
            String soql = "SELECT COUNT() FROM Lead";
            String urlStr = instanceUrl + "/services/data/v57.0/query?q=" +
                    URLEncoder.encode(soql, StandardCharsets.UTF_8);

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String response = reader.lines().reduce("", (acc, line) -> acc + line);
                JSONObject json = new JSONObject(response);
                return json.getInt("totalSize");
            }
        } catch (IOException e) {
            throw new TException(e);
        }
    }

    public String addLead(String fullName, double annualRevenue, String phone, String street,
                          String postalCode, String city, String country, String company, String state)
            throws LeadAlreadyExistsException, InvalidLeadParameterException, TException {
        try {
            ensureAuthenticated();

            // Découpage du nom complet en prénom + nom
            String[] parts = fullName.split(",", 2);
            String firstName = parts.length > 0 ? parts[0] : "";
            String lastName  = parts.length > 1 ? parts[1] : "";

            // Construction du JSON
            JSONObject leadJson = new JSONObject();
            leadJson.put("FirstName", firstName);
            leadJson.put("LastName", lastName);
            leadJson.put("Company", company);
            leadJson.put("AnnualRevenue", annualRevenue);
            leadJson.put("Phone", phone);
            leadJson.put("Street", street);
            leadJson.put("PostalCode", postalCode);
            leadJson.put("City", city);
            leadJson.put("Country", country);
            leadJson.put("State", state);

            // URL et connexion Salesforce
            String urlStr = instanceUrl + "/services/data/v57.0/sobjects/Lead/";
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(leadJson.toString().getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String response = reader.lines().reduce("", (acc, line) -> acc + line);
                    JSONObject json = new JSONObject(response);
                    return json.getString("id"); // Id Salesforce du Lead créé
                }
            } else if (responseCode == 400) {
                throw new InvalidLeadParameterException();
            } else if (responseCode == 409) {
                throw new LeadAlreadyExistsException();
            } else {
                throw new TException("Erreur Salesforce: " + responseCode);
            }

        } catch (IOException e) {
            throw new TException(e);
        }
    }


    public void deleteLead(String id) throws LeadNotFoundException, TException, IOException {
        try {
            ensureAuthenticated();
            String urlStr = instanceUrl + "/services/data/v57.0/sobjects/Lead/" + id;
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == 404) {
                throw new LeadNotFoundException();
            }
        } catch (IOException e) {
            throw new TException(e);
        }
    }
}
