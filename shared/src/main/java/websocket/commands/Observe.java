package websocket.commands;

public class Observe extends UserGameCommand{
    public Observe(int gameID, String authToken){
        super(CommandType.OBSERVE, authToken, gameID);
    }
}
