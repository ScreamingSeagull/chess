package Client;

import chess.*;
import exception.ResponseException;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ListGamesResult;
import model.result.RegisterResult;

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
                case "help" ->help();
                case "print" ->printG(out, false);
                case "quit" ->quitLogout();
                default -> System.out.println("Incorrect command entered.");
            }
        } catch (ResponseException e) {
            out.println(e.getMessage()); //999 error code because idk what the code is supposed to be
        }
    }
    public void clearDB() {
        server.clearDB();
        System.out.println("All databases wiped.");
    }
    public void register(String... params) {
        if (state.equals(State.SIGNEDOUT)){
            if (params.length != 3){
                System.out.println("Username, Password, and Email not provided.");
            }
            RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]); //Check parameters
            server.register(req);
            System.out.println("Successfully registered.");
        }
        else {
            System.out.println("Already logged in.");
        }
    }
    public void login(String... params) {
        if (state.equals(State.SIGNEDOUT)){
            LoginRequest req = new LoginRequest(params[0], params[1]); //Check parameters
            server.login(req);
            state = State.SIGNEDIN;
            System.out.println("Logged in.");
        }
        else {
            System.out.println("Already logged in.");
        }
    }
    public void quitLogout() { //Helper function, does not print message if not logged in
        if(state == State.SIGNEDIN) {
            logout();
        }
    }
    public void logout(){
        if (state.equals(State.SIGNEDIN)){
            server.logout();
            state = State.SIGNEDOUT;
            System.out.println("Logged out.");
        }
        else {
            System.out.println("Not currently logged in, cannot log out.");
        }
    }
    public void list(){ //Actually display the listed games
        if(state == state.SIGNEDIN) {
            ListGamesResult res = server.list();
            System.out.println("All games:");
            for (GameData g : res.games()){
                System.out.println(g);
            }
        }
        else {
            System.out.println("Not signed in.");
        }
    }
    public void create(String... params){
        if(state == state.SIGNEDIN) {
            if(params.length != 1) {
                System.out.println("Only enter the name of the game you wish to create.");
            }
            else{
                server.create(params[0]);
                System.out.println("Created new chess game " + params[0]);
            }
        }
        else {
            System.out.println("Not currently logged in.");
        }
    }
    public void join(String... params){ //GameID, playerColor
        if(state == state.SIGNEDIN) {
            if(params.length != 2) {
                System.out.println("Please input only Game ID and player color.");
            }
            else if (params[0] !=) {
                System.out.println("That game does not exist, please retry.");
            }
            else if (params[1] != "white" || params[1] != "black") {
                System.out.println("Please input a valid player color.");
            }
            else if (color already taken) {
                System.out.println("That player color is already taken.");
            }
            else {
                JoinGameRequest req = new JoinGameRequest(Integer.parseInt(params[0]), params[1]);
                server.join(req);
            }
        }
        else {
            System.out.println("Not currently logged in.");
        }
    }
    public void help(){
        out.println("lmao this loser needs help, imagine.");
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
    private Color convertCol(ChessPiece piece) {
        if(piece == null) {
            return null;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    private void printG(PrintStream out, boolean black) { //needs to flip black, white good
        printHorizontalBorder(out);
        if (black) { //Ascends from 1
            for (int boardRow = 0; boardRow < 8; ++boardRow) {
                drawSquare(out, null, null, (char) (boardRow + 49)); //Using ascii interpretation, int converted to char of set number
                for (int boardCol = 0; boardCol < 8; ++boardCol) {
                    if ((boardRow + boardCol) % 2 == 0) {
                        drawSquare(out, Color.white, convertCol(board.getPiece(boardRow + 1, boardCol + 1)), convert(board.getPiece(boardRow + 1, boardCol + 1)));
                    } else {
                        drawSquare(out, Color.black, convertCol(board.getPiece(boardRow + 1, boardCol + 1)), convert(board.getPiece(boardRow + 1, boardCol + 1)));
                    }
                }
                drawSquare(out, null, null, (char) (boardRow + 49));
                out.println(RESET_BG_COLOR);
            }
            printHorizontalBorder(out);
        } else{ //Descends from 8
            for (int boardRow = 7; boardRow >= 0; --boardRow) {
                drawSquare(out, null, null, (char) (boardRow + 49)); //Using ascii interpretation, int converted to char of set number
                for (int boardCol = 0; boardCol < 8; ++boardCol) {
                    if ((boardRow + boardCol) % 2 == 0) {
                        drawSquare(out, Color.white, convertCol(board.getPiece(boardRow + 1, boardCol + 1)), convert(board.getPiece(boardRow + 1, boardCol + 1)));
                    } else {
                        drawSquare(out, Color.black, convertCol(board.getPiece(boardRow + 1, boardCol + 1)), convert(board.getPiece(boardRow + 1, boardCol + 1)));
                    }
                }
                drawSquare(out, null, null, (char) (boardRow + 49));
                out.println(RESET_BG_COLOR);
            }
            printHorizontalBorder(out);
        }
    }
    private void drawSquare(PrintStream out, Color squareColor, Color pieceColor, Character c) {
        if (squareColor == Color.WHITE){
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else if (squareColor == Color.BLACK){
            out.print(SET_BG_COLOR_DARK_GREY);
        } else if (squareColor == null) {
            out.print(SET_BG_COLOR_BLACK);
        }
        if (pieceColor == Color.WHITE){
            out.print(SET_TEXT_COLOR_WHITE);
        } else if (pieceColor == Color.BLACK){
            out.print(SET_TEXT_COLOR_BLACK);
        } else if (pieceColor == null) {
            out.print(SET_TEXT_COLOR_WHITE);
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
