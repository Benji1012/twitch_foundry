import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.FileInputStream;

public class SimpleWebServer {
    public static void main(String[] args) throws Exception {
        // Load config from file
        Properties config = new Properties();
      

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        server.createContext("/panel.html", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/panel.html"));
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        });

        server.createContext("/config.html", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/config.html"));
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        });

        server.start();
        
        config.load(SimpleWebServer.class.getClassLoader().getResourceAsStream("config.properties"));

        String twitchToken = config.getProperty("twitchToken");
        String channelName = config.getProperty("channelName");
        String foundryApiUrl = config.getProperty("foundryApiUrl");
        String foundryApiKey = config.getProperty("foundryApiKey");
        
        String player1Name = config.getProperty("player1Name");
        String player2Name = config.getProperty("player2Name");
        String player3Name = config.getProperty("player3Name");
        String player4Name = config.getProperty("player4Name");
        String player5Name = config.getProperty("player5Name");
        String player6Name = config.getProperty("player6Name");
        
        
        new TwitchDiceBot(twitchToken, channelName, foundryApiUrl, foundryApiKey,player1Name,player2Name,player3Name,player4Name,player5Name,player6Name);
        System.out.println("Server started at http://localhost:8080/");
    }
}
