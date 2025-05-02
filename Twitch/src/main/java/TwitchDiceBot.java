import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class TwitchDiceBot {

    private final TwitchClient twitchClient;
    private final FoundryApiClient foundryClient;
    private final String channelName;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    private Player player6;

    public TwitchDiceBot(String twitchToken, String channelName, String foundryApiUrl , String foundryApiKey, String player1Name, String player2Name, 
    		String player3Name, String player4Name, String player5Name, String player6Name) throws Exception {
        this.channelName = channelName;

        // Twitch Client
        this.twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withChatAccount(oauthCredential(twitchToken))
            .build();

        this.foundryClient = new FoundryApiClient(foundryApiUrl, foundryApiKey);

        twitchClient.getChat().joinChannel(channelName);
       // downloadIds();
        if(!player1Name.equals("")) {
        	player1 = new Player(player1Name, this.foundryClient);
        }
        if(!player2Name.equals("")) {
        	player2 = new Player(player2Name, this.foundryClient);
        }
        if(!player3Name.equals("")) {
        	player3 = new Player(player3Name, this.foundryClient);
        }
        if(!player4Name.equals("")) {
        	player4 = new Player(player4Name, this.foundryClient);
        }
        if(!player5Name.equals("")) {
        	player5 = new Player(player5Name, this.foundryClient);
        }
        if(!player6Name.equals("")) {
        	player6 = new Player(player6Name, this.foundryClient);
        }
        System.out.println("Player1: "+player1.toString());
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
            }else if (message.startsWith("!"+player1.getName())) {
            	 String response = player1.toString();  // this will call your `toString()` method
                 twitchClient.getChat().sendMessage(channelName, response);
            }else if (message.startsWith("!"+player2.getName())) {
	           	 String response = player2.toString();  // this will call your `toString()` method
	             twitchClient.getChat().sendMessage(channelName, response);
	        }else if (message.startsWith("!"+player3.getName())) {
		       	 String response = player3.toString();  // this will call your `toString()` method
		         twitchClient.getChat().sendMessage(channelName, response);
		    }else if (message.startsWith("!"+player4.getName())) {
			   	 String response = player4.toString();  // this will call your `toString()` method
			     twitchClient.getChat().sendMessage(channelName, response);
			}else if (message.startsWith("!"+player5.getName())) {
				 String response = player5.toString();  // this will call your `toString()` method
			     twitchClient.getChat().sendMessage(channelName, response);
			}else if (message.startsWith("!"+player6.getName())) {
				 String response = player6.toString();  // this will call your `toString()` method
			     twitchClient.getChat().sendMessage(channelName, response);
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
    
   
	public TwitchClient getTwitchClient() {
		return twitchClient;
	}

	public FoundryApiClient getFoundryClient() {
		return foundryClient;
	}

	public String getChannelName() {
		return channelName;
	}
	
	public Player getPlayer1() { return player1; }
	public Player getPlayer2() { return player2; }
	public Player getPlayer3() { return player3; }
	public Player getPlayer4() { return player4; }
	public Player getPlayer5() { return player5; }
	public Player getPlayer6() { return player6; }
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
