import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class FoundryApiClient {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String apiKey;
    private final String clientId;

    public FoundryApiClient(String baseUrl, String apiKey) throws Exception {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
		this.clientId = getClienId();
    }
    
    public synchronized String getClienId() throws Exception {

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/clients"))
          .header("x-api-key", apiKey)
          .header("Content-Type", "application/json")
          .GET()
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println("Response: "+response.body());
   // Parse the response body
      JSONObject json = new JSONObject(response.body());
      JSONArray clients = json.getJSONArray("clients");

      if (clients.length() > 0) {
          JSONObject firstClient = clients.getJSONObject(0);
          String clientId = firstClient.getString("id");
          System.out.println("ClientId: " + clientId);
          return clientId;
      } else {
          throw new RuntimeException("No clients found!");
      }
  }

    public synchronized void rollDice(String formula, String viewerName) throws Exception {
//        String json = String.format("{\"formula\":\"%s\",", formula);
//         json += String.format("\"itemUuid\":\"%s\",", "");
//         json += String.format("\"flavor\":\"%s\",", viewerName);
//         json += String.format("\"createChatMessage\":\"%s\",", true);
//         json += String.format("\"target\":\"%s\",", "");
//         json += String.format("\"speaker\":\"%s\",", "");
//         json += String.format("\"whisper\":\"%s\"}", "[]");
         JSONObject payload = new JSONObject();
         payload.put("formula", formula);
         payload.put("flavor", viewerName);
         payload.put("target", "");
         payload.put("speaker", "");
         payload.put("itemUuid", "");
         payload.put("createChatMessage", true);
         payload.put("whisper", new org.json.JSONArray());
         
        System.out.println("json: "+payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/roll?clientId=" + clientId))
            .header("x-api-key", apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Dice roll result: " + response.body());
    }
}
