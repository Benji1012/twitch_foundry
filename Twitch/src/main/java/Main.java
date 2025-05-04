import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        // Load config from file
        Properties config = new Properties();
      

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        
        
        config.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));

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

        server.createContext("/config.html", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/config.html"));
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        });
        
        TwitchDiceBot bot = new TwitchDiceBot(twitchToken, channelName, foundryApiUrl, foundryApiKey,player1Name,player2Name,player3Name,player4Name,player5Name,player6Name);
        
        server.createContext("/players", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Content-Type", "application/json");

                ObjectMapper mapper = new ObjectMapper();

                // Filter non-null players
                java.util.ArrayList<Player> players = new java.util.ArrayList<Player>();
                if (bot.getPlayer1() != null) players.add(bot.getPlayer1());
                if (bot.getPlayer2() != null) players.add(bot.getPlayer2());
                if (bot.getPlayer3() != null) players.add(bot.getPlayer3());
                if (bot.getPlayer4() != null) players.add(bot.getPlayer4());
                if (bot.getPlayer5() != null) players.add(bot.getPlayer5());
                if (bot.getPlayer6() != null) players.add(bot.getPlayer6());
                System.out.println("Players fetched: " + mapper.writeValueAsString(players));

                byte[] json = mapper.writeValueAsBytes(players);
                exchange.sendResponseHeaders(200, json.length);
                OutputStream os = exchange.getResponseBody();
                os.write(json);
                os.close();
            }
        });
        
        server.createContext("/panel.html", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/panel.html"));
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        });

        server.start();
        
       
        
        
        
        System.out.println("Server started at http://localhost:8080/");
    }
}
