package org.example.client.service.rest;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.example.client.service.config.Config;
import org.example.client.service.exceptions.LeadNotFoundException;
import org.example.client.service.rest.json.JsonToClientExceptionConversor;
import org.example.client.service.rest.json.JsonToLeadConversor;
import org.example.client.service.dto.LeadDto;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class VirtualCRMAPI implements IVirtualCRMAPI {

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


    @Override
    public String addLead(String fullName, double annualRevenue, String phone, String street, String postalCode, String city, String country, String company, String state) {

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

            InputStream bodyStream = new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8));

            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .post(api_url + "addLead")
                    .bodyStream(bodyStream, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return response.getEntity().getContent().toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteLead(String id) throws LeadNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .delete(api_url + "/deleteLead/" + id)
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return response.getEntity().getContent().toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getLeads() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/getLeads") //TODO : get ou autre méthode ?
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getLeadById(String id) {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/getLeadById/" + id)
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDto(response.getEntity().getContent()).toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state) {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url
                            + "/leads?lowAnnualRevenue=" + URLEncoder.encode(String.valueOf(lowAnnualRevenue), "UTF-8")
                            + "&highAnnualRevenue=" +  URLEncoder.encode(String.valueOf(highAnnualRevenue), "UTF-8")
                            +  "&state=" + URLEncoder.encode(state, "UTF-8"))
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findLeadsByDate(String startDate, String endDate) {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url
                            + "/leads?startDate=" + URLEncoder.encode(String.valueOf(startDate), "UTF-8")
                            + "&endDate=" +  URLEncoder.encode(String.valueOf(endDate), "UTF-8"))
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String countLeads() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/countLeads")
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
//            validateStatusCode(HttpStatus.SC_CREATED, response);

            return response.getEntity().getContent().toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
