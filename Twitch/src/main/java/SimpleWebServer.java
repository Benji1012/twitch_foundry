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
        
        
        new TwitchDiceBot(twitchToken, channelName, foundryApiUrl, foundryApiKey);
        System.out.println("Server started at http://localhost:8080/");
    }
}
