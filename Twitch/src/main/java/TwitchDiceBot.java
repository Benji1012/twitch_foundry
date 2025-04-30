import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class TwitchDiceBot {

    private final TwitchClient twitchClient;
    private final FoundryApiClient foundryClient;
    private final String channelName;

    public TwitchDiceBot(String twitchToken, String channelName, String foundryApiUrl , String foundryApiKey) throws Exception {
        this.channelName = channelName;

        // Twitch Client
        this.twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withChatAccount(oauthCredential(twitchToken))
            .build();

        // Foundry API Client (új, HTTP-alapú)
//        clientId = "foundry-"+clientId ;
        this.foundryClient = new FoundryApiClient(foundryApiUrl, foundryApiKey);

        twitchClient.getChat().joinChannel(channelName);
       
        registerListeners();
    }

    private static com.github.philippheuer.credentialmanager.domain.OAuth2Credential oauthCredential(String token) {
        return new com.github.philippheuer.credentialmanager.domain.OAuth2Credential("twitch", token);
    }

    private synchronized void registerListeners() {
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            String message = event.getMessage();
            String viewerName = event.getUser().getName()+" Roll";
            System.out.println("Ez az üzenet: "+ message);
            if (message.startsWith("!roll")) {
                String diceCommand = parseDiceCommand(message);
                if (diceCommand != null) {
                    System.out.println("Dice roll request: " + diceCommand);
                    try {
                        foundryClient.rollDice(diceCommand, viewerName);  // <<=== HTTP hívás itt!
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String parseDiceCommand(String message) {
        if (message.length() <= 6) {
            return null;
        }
        String command = message.substring(6).trim();
     //   command = command.replace('k', 'd');
        if (command.matches("\\d+d.*")) {
            return command;
        }
        return null;
    }
}

//    public static void main(String[] args) throws Exception {
//        String twitchToken = "oauth:mtrc34w0ixhqykb2qytp2az964m6zd"; // Twitch Bot token
//        String channelName = "nagyben"; // Twitch csatorna
//        String foundryApiUrl = "https://foundryvtt-rest-api-relay.fly.dev"; // HTTP API URL
//        String foundryApiKey = "c8955f23d65938c19fd5b86bd42e799f"; // API kulcs (Simple API modultól)
//        String clientId = "YanyeoFQhhQ0hMyZ"; // client id
//
//        new TwitchDiceBot(twitchToken, channelName, foundryApiUrl, foundryApiKey, clientId);
//    }
//}
