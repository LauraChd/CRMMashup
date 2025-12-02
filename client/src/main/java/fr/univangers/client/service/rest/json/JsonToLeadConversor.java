package fr.univangers.client.service.rest.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Classe qui permet de convertir des contenus JSON représentant des leads en
 * chaînes lisibles.
 */
public class JsonToLeadConversor {

    /**
     * Convertit un seul Lead en String lisible.
     *
     * @param content Flux JSON.
     * @return Représentation textuelle du lead.
     */
    public static String toLeadDto(InputStream content) {
        String jsonText = convertStreamToString(content);
        JSONObject obj = new JSONObject(jsonText);

        return String.format(
                "Lead[id=%s, %s %s, company=%s, revenue=%.2f, phone=%s, address=%s, %s %s, %s, %s, date=%s]",
                obj.optString("id"),
                obj.optString("firstName"),
                obj.optString("lastName"),
                obj.optString("company"),
                obj.optDouble("annualRevenue"),
                obj.optString("phone"),
                obj.optString("street"),
                obj.optString("postalCode"),
                obj.optString("city"),
                obj.optString("state"),
                obj.optString("country"),
                obj.optString("date"));
    }

    /**
     * Convertit une liste de Leads en String lisible.
     *
     * @param content Flux JSON.
     * @return Représentation textuelle des leads.
     */
    public static String toLeadDtos(InputStream content) {
        String jsonText = convertStreamToString(content);
        JSONArray array = new JSONArray(jsonText);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            long epoch = obj.optLong("creationDate");
            String date = Instant.ofEpochMilli(epoch)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            sb.append(String.format(
                    "Lead[id=%s, %s %s, company=%s, revenue=%.2f, phone=%s, address=%s, %s %s, %s %s, date=%s]%n",
                    obj.optString("id"),
                    obj.optString("firstName"),
                    obj.optString("lastName"),
                    obj.optString("company"),
                    obj.optDouble("annualRevenue"),
                    obj.optString("phone"),
                    obj.optString("street"),
                    obj.optString("postalCode"),
                    obj.optString("city"),
                    obj.optString("state"),
                    obj.optString("country"),
                    date));
        }
        return sb.toString();
    }

    /**
     * Pour lire un InputStream en String.
     *
     * @param is Flux d'entrée.
     * @return Contenu sous forme de chaîne.
     */
    private static String convertStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
        String text = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        scanner.close();
        return text;
    }
}
