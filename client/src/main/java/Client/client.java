package Client;

import chess.*;
import exception.ResponseException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import java.nio.charset.StandardCharsets;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import static java.sql.Types.NULL;
import static ui.EscapeSequences.*;

public class client {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    ChessBoard board = new ChessBoard();
    public client(String domainName) throws URISyntaxException, IOException {
        server = new ServerFacade(domainName);
        board.resetBoard();
    }
    public void eval(String input) { //This needs to return data of differing kinds, condense into json?
        String[] line = input.toLowerCase().split(" ");
        String cmd;
        if (line.length == 0) {
            cmd = "help";
        }
        else {
            cmd = line[0];
        }
        String[] params = Arrays.copyOfRange(line, 1, line.length);
        try {
            switch(cmd) {
                case "wipeall"->clearDB();
                case "register"->register(params);
                case "login"->login(params);
                case "logout"->logout();
                case "list"->list();
                case "create"->create(params);
                case "join"->join(params);
                default->help();
            }
        } catch (ResponseException e) {
            throw new ResponseException(999, "Incorrect command entered"); //999 error code because idk what the code is supposed to be
        }
    }
    public void clearDB() {
        printG(out);
        //server.clearDB();
    }
    public void register(String... params) {
        RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]); //Check parameters
        server.register(req);
    }
    public void login(String... params) {
        LoginRequest req = new LoginRequest(params[0], params[1]); //Check parameters
        server.login(req);
    }
    public void logout(){
        server.logout();
    }
    public void list(){
        server.list();

    }
    public void create(String... params){
        CreateGameRequest req = new CreateGameRequest(params[0], params[1]); //Check parameters
        server.create(req);
    }
    public void join(String... params){
        JoinGameRequest req = new JoinGameRequest(Integer.parseInt(params[0]), params[1]); //Check parameters
        server.join(req);
    }
    public void help(){
        //Not handled by server, keep local
    }
    private void printHorizontalBorder(PrintStream out) {
        drawSquare(out, null, null, null);
        for (int i = 0; i<8; i++) {
            drawSquare(out, null, null, (char)('A' + i));
        }
        drawSquare(out, null, null, null);
        out.println(RESET_BG_COLOR);
    }
    private char convert(ChessPiece piece) {
        if (piece == null || piece.getPieceType() == null) {
            return ' ';
        }
        switch(piece.getPieceType()) {
            case KING:
                return 'K';
            case QUEEN:
                return 'q';
            case ROOK:
                return 'r';
            case BISHOP:
                return 'b';
            case KNIGHT:
                return 'k';
            case PAWN:
                return 'p';
            default:
                return ' ';
        }
    }

    private void printG(PrintStream out) {
        printHorizontalBorder(out);
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            drawSquare(out, null, null, (char)(boardRow + 49)); //Using ascii interpretation, int converted to char of set number
            for (int boardCol = 0; boardCol < 8; ++boardCol) {
                if ((boardRow + boardCol) % 2 == 0) {
                    drawSquare(out, Color.white, Color.BLACK, convert(board.getPiece(boardRow + 1, boardCol + 1)));
                } else {
                    drawSquare(out, Color.black, Color.WHITE, ' ');
                }
            }
            drawSquare(out, null, null, (char)(boardRow + 49));
            out.println(RESET_BG_COLOR);
        }
        printHorizontalBorder(out);
    }
    private void drawSquare(PrintStream out, Color squareColor, Color pieceColor, Character c) {
        if (squareColor == Color.WHITE){
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else if (squareColor == Color.BLACK){
            out.print(SET_BG_COLOR_DARK_GREY);
        } else if (squareColor == null) {
            out.print(SET_BG_COLOR_WHITE);
        }
        if (pieceColor == Color.WHITE){
            out.print(SET_TEXT_COLOR_WHITE);
        } else if (pieceColor == Color.BLACK){
            out.print(SET_TEXT_COLOR_BLACK);
        } else if (pieceColor == null) {
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
        }
        if (c == null) {
            out.print("\u2005\u2005 \u2005\u2005");
        } else {
            out.print("\u2005\u2005" + c + "\u2005\u2005");
            }
    }
}

//    private String stringBoard(ChessBoard board, Collection<ChessPosition> locations, boolean black){
//        int up = black ? 1:8;
//        int down = black ? 8:1;
//        int diff = black ? -1:1;
//        StringBuilder sb = new StringBuilder();
//        boolean alt = true;
//        buildUp(sb, black);
//        boolean altTile = true; //switch between the two each time, boolean = !boolean
//        buildTile(sb, altTile, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN, true);
//        sb.append(RESET_BG_COLOR + '\n');
//        return "null";
//    }
//    private void buildUp(StringBuilder sb, boolean black) {
//        int up = black ? 1:8;
//        int down = black ? 8:1;
//        char diff = (char) (black ? -1:1);
//        buildBorder(sb);
//    }
//    private void buildBorder(StringBuilder sb) {
//        sb.append(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_WHITE);
//        sb.append("dimensions");
//        sb.append("*");
//        sb.append("dimensions");
//    }
//    private void buildTile(StringBuilder sb, boolean altColor, ChessGame.TeamColor teamColor, ChessPiece.PieceType piece, boolean selected) {
//        if (selected) {
//            sb.append("selected");
//        } else if (altColor) {
//            sb.append("altcolor");
//        } else {
//            sb.append("basic color");
//        }
//        if (teamColor == ChessGame.TeamColor.WHITE) {
//            sb.append(SET_TEXT_COLOR_WHITE);
//        } else {
//            sb.append(SET_TEXT_COLOR_BLACK);
//        }
//    }
