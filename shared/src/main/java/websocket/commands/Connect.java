package websocket.commands;

public class Connect extends UserGameCommand{
    public Connect(int gameID, String authToken){
        super(CommandType.CONNECT, authToken, gameID);
    }
}
