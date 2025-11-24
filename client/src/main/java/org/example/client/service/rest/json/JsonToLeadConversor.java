package org.example.client.service.rest.json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JsonToLeadConversor {

    // Convertit un seul Lead en String lisible
    public static String toLeadDto(InputStream content) {
        String jsonText = convertStreamToString(content);
        JSONObject obj = new JSONObject(jsonText);

        return String.format(
                "Lead[id=%d, %s %s, company=%s, revenue=%.2f, phone=%s, address=%s %s %s %s %s]",
                obj.optInt("id"),
                obj.optString("firstName"),
                obj.optString("lastName"),
                obj.optString("company"),
                obj.optDouble("annualRevenue"),
                obj.optString("phone"),
                obj.optString("street"),
                obj.optString("postalCode"),
                obj.optString("city"),
                obj.optString("state"),
                obj.optString("country")
        );
    }

    // Convertit une liste de Leads en String lisible
    public static String toLeadDtos(InputStream content) {
        String jsonText = convertStreamToString(content);
        JSONArray array = new JSONArray(jsonText);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            sb.append(String.format(
                    "Lead[id=%d, %s %s, company=%s, revenue=%.2f, phone=%s, address=%s %s %s %s %s]%n",
                    obj.optInt("id"),
                    obj.optString("firstName"),
                    obj.optString("lastName"),
                    obj.optString("company"),
                    obj.optDouble("annualRevenue"),
                    obj.optString("phone"),
                    obj.optString("street"),
                    obj.optString("postalCode"),
                    obj.optString("city"),
                    obj.optString("state"),
                    obj.optString("country")
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

