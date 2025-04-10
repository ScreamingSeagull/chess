package websocket.commands;

public class Observe extends UserGameCommand{
    private final int gameID;
    private final String authToken;
    public Observe(int gameID, String authToken){
        super(CommandType.OBSERVE, authToken, gameID);
        this.gameID = gameID;
        this.authToken = authToken;
    }
}
