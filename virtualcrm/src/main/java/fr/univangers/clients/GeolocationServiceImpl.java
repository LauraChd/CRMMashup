package fr.univangers.clients;

import fr.univangers.model.GeographicPointDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.univangers.model.VirtualLeadDto;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * TODO
 */
public class GeolocationServiceImpl {
    public GeographicPointDto lookup(VirtualLeadDto virtualLead) throws IOException {
        GeographicPointDto geographicPoint = null;

        String urlStr = "https://nominatim.openstreetmap.org/search?q=" +
                "city=" + virtualLead.getCity() + "&country=" + virtualLead.getCountry() + "&postalcode=" + virtualLead.getPostalCode() +
                "&street=" + virtualLead.getStreet() + "&format=json&limit=1";

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
            geographicPoint =  new GeographicPointDto(Double.parseDouble(lat), Double.parseDouble(lon));
        } else {
            System.out.println("Aucun résultat trouvé.");
        }
        return geographicPoint;
    }
}
