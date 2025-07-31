import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class GeminiGameHelper {
    private static final String API_KEY = "AIzaSyAXGjQorgCFW_K01N_ebZP7QCAHxfF0IxM";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public static String askForSuggestion(String board, String colorToMove) {
        try {
            JsonObject payload = new JsonObject();
            JsonArray contents = new JsonArray();
            JsonObject message = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", "Here is a chess board:\n" + board + "\nWhat should " + colorToMove + " play next and why?");
            parts.add(textPart);
            message.add("parts", parts);
            contents.add(message);
            payload.add("contents", contents);

            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine,Builder = "";
            while ((inputLine = in.readLine()) != null) Builder += inputLine;
            in.close();

            JsonObject response = JsonParser.parseString(Builder).getAsJsonObject();
            return response
                    .getAsJsonArray("candidates").get(0)
                    .getAsJsonObject().get("content")
                    .getAsJsonObject().getAsJsonArray("parts")
                    .get(0).getAsJsonObject().get("text").getAsString();

        } catch (Exception e) {
            return "Error fetching suggestion: " + e.getMessage();
        }
    }
}
