package fr.univangers.clients;

import fr.univangers.model.GeographicPointDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import fr.univangers.model.VirtualLeadDto;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Client pour le service de géolocalisation.
 */
public class GeoLocalisationServiceClient {
    /**
     * Recherche les coordonnées géographiques d'un lead.
     *
     * @param virtualLead Le lead pour lequel chercher les coordonnées.
     * @return Les coordonnées géographiques trouvées, ou vide si non trouvé.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    public Optional<GeographicPointDto> lookup(VirtualLeadDto virtualLead) throws IOException {
        GeographicPointDto geographicPoint = null;

        String urlStr = "https://nominatim.openstreetmap.org/search?"
                + "city=" + URLEncoder.encode(virtualLead.getCity(), StandardCharsets.UTF_8)
                + "&country=" + URLEncoder.encode(virtualLead.getCountry(), StandardCharsets.UTF_8)
                + "&postalcode=" + URLEncoder.encode(virtualLead.getPostalCode(), StandardCharsets.UTF_8)
                + "&street=" + URLEncoder.encode(virtualLead.getStreet(), StandardCharsets.UTF_8)
                + "&format=json&limit=1";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "JavaApp");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONArray jsonArray = new JSONArray(response.toString());
        if (!jsonArray.isEmpty()) {
            JSONObject obj = jsonArray.getJSONObject(0);
            String lat = obj.getString("lat");
            String lon = obj.getString("lon");
            geographicPoint = new GeographicPointDto(Double.parseDouble(lat), Double.parseDouble(lon));
        }

        return Optional.ofNullable(geographicPoint);
    }
}
