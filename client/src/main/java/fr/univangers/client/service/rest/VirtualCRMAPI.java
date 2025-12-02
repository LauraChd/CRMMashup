package fr.univangers.client.service.rest;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import fr.univangers.client.service.config.Config;
import fr.univangers.client.service.exceptions.InvalidParametersException;
import fr.univangers.client.service.exceptions.LeadNotFoundException;
import fr.univangers.client.service.rest.json.JsonToClientExceptionConversor;
import fr.univangers.client.service.rest.json.JsonToLeadConversor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Implémentation REST de l’API VirtualCRM.
 * Cette classe communique avec le serveur via HTTP pour manipuler les leads.
 */
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

    /**
     * Ajoute un lead via un appel HTTP POST.
     *
     * @param fullName      Nom complet.
     * @param annualRevenue Revenu annuel.
     * @param phone         Téléphone.
     * @param street        Rue.
     * @param postalCode    Code postal.
     * @param city          Ville.
     * @param country       Pays.
     * @param company       Entreprise.
     * @param state         État.
     * @return L'ID du lead ajouté.
     * @throws InvalidParametersException Si les paramètres sont invalides.
     */
    @Override
    public String addLead(String fullName, double annualRevenue, String phone,
            String street, String postalCode, String city,
            String country, String company, String state)
            throws InvalidParametersException {

        try {
            // Construction du body "form-urlencoded"
            String params = "fullName=" + URLEncoder.encode(fullName, StandardCharsets.UTF_8) +
                    "&annualRevenue=" + URLEncoder.encode(String.valueOf(annualRevenue), StandardCharsets.UTF_8) +
                    "&phone=" + URLEncoder.encode(phone, StandardCharsets.UTF_8) +
                    "&street=" + URLEncoder.encode(street, StandardCharsets.UTF_8) +
                    "&postalCode=" + URLEncoder.encode(postalCode, StandardCharsets.UTF_8) +
                    "&city=" + URLEncoder.encode(city, StandardCharsets.UTF_8) +
                    "&country=" + URLEncoder.encode(country, StandardCharsets.UTF_8) +
                    "&company=" + URLEncoder.encode(company, StandardCharsets.UTF_8) +
                    "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .post(api_url + "/addLead")
                    .bodyString(params, ContentType.APPLICATION_FORM_URLENCODED)
                    .execute()
                    .returnResponse();

            // (optionnel) tu peux checker le status ici avec validateStatusCode(...)
            // validateStatusCode(HttpStatus.SC_CREATED, response);

            // On lit juste la réponse brute (l'id renvoyé par le contrôleur)
            return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8).trim();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime un lead via son identifiant.
     *
     * @param id ID du lead.
     * @return Message de confirmation.
     * @throws InvalidParametersException Si les paramètres sont invalides.
     * @throws LeadNotFoundException      Si le lead n'est pas trouvé.
     */
    @Override
    public String deleteLead(String id)
            throws InvalidParametersException, LeadNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .delete(api_url + "/leads/" + id)
                    .execute()
                    .returnResponse();

            int code = response.getCode();

            if (code == HttpStatus.SC_NO_CONTENT) { // 204
                return "Lead " + id + " deleted";
            } else if (code == HttpStatus.SC_NOT_FOUND) {
                // conversion JSON → exception métier
                throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
            } else {
                throw new RuntimeException("HTTP error: " + code);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Récupère l’ensemble des leads.
     *
     * @return Liste des leads.
     */
    @Override
    public String getLeads() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/leads")
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
            // validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Récupère un lead à partir de son identifiant.
     *
     * @param id ID du lead.
     * @return Le lead trouvé.
     * @throws LeadNotFoundException Si le lead n'est pas trouvé.
     */
    @Override
    public String getLeadById(String id)
            throws LeadNotFoundException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/leads/" + id)
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
            // validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDto(response.getEntity().getContent()).toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recherche de leads par revenus et par état.
     *
     * @param lowAnnualRevenue  Revenu minimum.
     * @param highAnnualRevenue Revenu maximum.
     * @param state             État.
     * @return Liste des leads trouvés.
     * @throws InvalidParametersException Si les paramètres sont invalides.
     */
    @Override
    public String findLeads(double lowAnnualRevenue, double highAnnualRevenue, String state)
            throws InvalidParametersException {
        try {
            String url = api_url + "/findLeads"
                    + "?lowAnnualRevenue=" + URLEncoder.encode(String.valueOf(lowAnnualRevenue), StandardCharsets.UTF_8)
                    + "&highAnnualRevenue="
                    + URLEncoder.encode(String.valueOf(highAnnualRevenue), StandardCharsets.UTF_8)
                    + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(url)
                    .execute()
                    .returnResponse();

            return JsonToLeadConversor.toLeadDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recherche de leads entre deux dates.
     *
     * @param startDate Date de début.
     * @param endDate   Date de fin.
     * @return Liste des leads trouvés.
     * @throws InvalidParametersException Si les paramètres sont invalides.
     */
    @Override
    public String findLeadsByDate(String startDate, String endDate)
            throws InvalidParametersException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url
                            + "/leads?startDate=" + URLEncoder.encode(String.valueOf(startDate), "UTF-8")
                            + "&endDate=" + URLEncoder.encode(String.valueOf(endDate), "UTF-8"))
                    .execute()
                    .returnResponse();

            // Vérif retour HTTP TODO
            // validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToLeadConversor.toLeadDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne le nombre total de leads.
     *
     * @return Le nombre de leads.
     */
    @Override
    public String countLeads() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/countLeads")
                    .execute()
                    .returnResponse();

            // le contrôleur renvoie juste un int dans le body
            return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8).trim();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fusionne les leads Salesforce et InternalCRM.
     *
     * @return Résultat de la fusion.
     */
    @Override
    public String merge() {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request
                    .get(api_url + "/merge")
                    .execute()
                    .returnResponse();

            return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Vérifie le code HTTP retourné et déclenche l’exception correspondante.
     *
     * @param successCode Code de succès attendu.
     * @param response    Réponse HTTP.
     * @throws Exception En cas d'erreur.
     */
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
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
