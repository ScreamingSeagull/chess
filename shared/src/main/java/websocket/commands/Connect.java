package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    private final String playerColor;
    public Connect(int gameID, String playerColor, String authToken){
        super(CommandType.CONNECT, authToken, gameID);
        this.playerColor = playerColor;
    }
    public String getPlayerColor(){
        return playerColor;
    }
}
