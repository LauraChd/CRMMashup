package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.client.deprecated_movies.JsonToClientMovieDtoConversor;
import org.example.client.deprecated_movies.ObjectMapperFactory;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class VirtualCRMAPI {

    Config config;

    {
        try {
            config = new Config("config.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    String api_url = config.getProperty("API_URL");

    CloseableHttpClient httpclient = HttpClients.createDefault();


    public int addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) {

        try {
            //Construction JSON
            JSONObject json = new JSONObject();
            json.put("fullName", fullName);
            json.put("annualRevenue", annualRevenue);
            json.put("phone", phone);
            json.put("street", street);
            json.put("postalCode", postalCode);
            json.put("city", city);
            json.put("country", country);
            json.put("company", company);
            json.put("state", state);

            // Conversion en InputStream si ton code actuel le demande
            InputStream bodyStream = new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));

            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .post(api_url + "addLead")
                    .bodyStream(bodyStream, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDto(response.getEntity().getContent()).getId(); //TODO

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public int deleteLead(String id) {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/countLeads")
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return Integer.valueOf(response.getEntity().getContent().toString()); //TODO

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<LeadDto> getLeads() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/getLeads")
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDto(response.getEntity().getContent()).getId(); //TODO

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int countLeads() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/countLeads")
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return Integer.valueOf(response.getEntity().getContent().toString()); //TODO

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
