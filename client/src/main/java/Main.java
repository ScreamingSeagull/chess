import client.Repl;
import chess.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var serverUrl = "http://localhost:8080";
        if (args.length >= 1) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }
}