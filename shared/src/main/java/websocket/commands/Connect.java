package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    private final int gameID;
    private final String playerColor;
    private final String authToken;
    public Connect(int gameID, String playerColor, String authToken){
        super(CommandType.CONNECT, authToken, gameID);
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
