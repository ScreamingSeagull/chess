package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    private final int gameID;
    private final ChessMove move;
    public MakeMove(int gameID, String authToken, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.gameID = gameID;
        this.move = move;
    }
    public ChessMove getMove() {
        return move;
    }
}
