package fr.univangers.client.service.rest.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class JsonToLeadConversor {

    // Convertit un seul Lead en String lisible
    public static String toLeadDto(InputStream content) {
        String jsonText = convertStreamToString(content);
        JSONObject obj = new JSONObject(jsonText);

        String date = getCreationDateFromJSON(obj);

        return String.format(
                "Lead[id=%s, %s %s, company=%s, revenue=%.2f, phone=%s, address=%s, %s %s, %s, %s, date=%s, geoLoc=%s]",
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
                date,
                obj.optString("geographicPointDto"));
    }

    private static String getCreationDateFromJSON(JSONObject obj) {
        String rawDate = obj.optString("creationDate");
        String date;
        try {
            long epoch = Long.parseLong(rawDate);
            date = Instant.ofEpochMilli(epoch)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (NumberFormatException e) {
            if (rawDate.isEmpty()) {
                date = Instant.ofEpochMilli(0)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } else {
                date = rawDate;
            }
        }
        return date;
    }

    // Convertit une liste de Leads en String lisible
    public static String toLeadDtos(InputStream content) {
        String jsonText = convertStreamToString(content);
        JSONArray array = new JSONArray(jsonText);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            String date = getCreationDateFromJSON(obj);

            sb.append(String.format(
                    "Lead[id=%s, %s %s, company=%s, revenue=%.2f, phone=%s, address=%s, %s %s, %s %s, date=%s, geoLoc=%s]%n",
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
                    date,
                    obj.optString("geographicPointDto")
            ));
        }
        return sb.toString();
    }

    // Utilitaire pour lire un InputStream en String
    private static String convertStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
        String text = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        scanner.close();
        return text;
    }
}

