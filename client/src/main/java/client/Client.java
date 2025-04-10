package client;

import chess.*;

import client.websocket.NotificationHandler;
import exception.ResponseException;
import model.GameData;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ListGamesResult;
import websocket.Notification;

import java.nio.charset.StandardCharsets;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Client implements NotificationHandler {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    ChessBoard board = new ChessBoard();
    ArrayList<GameData> gameDataArrayList = new ArrayList<>();
    public Client(String domainName, String serverUrl) throws URISyntaxException, IOException {
        this.serverUrl = serverUrl;
        server = new ServerFacade(domainName, this);
        board.resetBoard();
    }
    public void eval(String input) {
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
                case "clear"->clearDB();
                case "register"->register(params);
                case "login"->login(params);
                case "logout"->logout();
                case "list"->list();
                case "create"->create(params);
                case "join"->join(params);
                case "help" ->help();
                case "print" ->printG(out, true);
                case "watch" ->observe(params);
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
        state = State.SIGNEDOUT;
    }
    public void register(String... params) {
        if (state.equals(State.SIGNEDOUT)){
            if (params.length != 3){
                System.out.println("Username, Password, and Email not provided.");
            } else{
                RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]); //Check parameters
                server.register(req);
                login(params[0], params[1]);
                System.out.println("Successfully registered."); //Call login to auto login after registration?
            }
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
        System.out.println("Logged out.");
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
    public void createList(){
        ListGamesResult res = server.list();
        gameDataArrayList.clear();
        gameDataArrayList.addAll(res.games());
    }
    public void list(){ //Actually display the listed games
        if(state == state.SIGNEDIN) {
            createList();
            if (gameDataArrayList.isEmpty()) {
                System.out.println("No current games.");
            } else {
                System.out.println("All games:"); //Convert between game ID and number in list
                for (int i = 0; i < gameDataArrayList.size(); i++) {
                    System.out.println("Chess game " + (i+1) + ": " + gameDataArrayList.get(i));
                }
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
    public void join(String... params){
        if(state == state.SIGNEDIN) {
            if(params.length != 2) {
                System.out.println("Please input only Game ID and player color.");
            }
            else if (params[1].equals("white") || params[1].equals("black")) {
                try {
                    if (Integer.parseInt(params[0]) > gameDataArrayList.size() || Integer.parseInt(params[0]) - 1 < 0) {
                        System.out.println("Invalid gameID.");
                    } else {
                        joinGame(params);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please input a valid gameID");
                }
            }
            else {System.out.println("Please input a valid player color.");}
        }
        else {
            System.out.println("Not currently logged in.");
        }
    }

    private void joinGame(String[] params) {
        int gameid = (gameDataArrayList.get(Integer.parseInt(params[0]) - 1).gameID());
        JoinGameRequest req = new JoinGameRequest(gameid, params[1]);
        server.join(req);
        System.out.println("Joined game.");
        if (params[1].equals("white")){
            printG(out, false);
        } else {
            printG(out, true);
        }
    }

    public void observe(String... params) {
        if(state == state.SIGNEDIN) {
            if(params.length != 1) {
                System.out.println("Please input only Game ID");
            }
            try {
                int game = Integer.parseInt(params[0]);
                System.out.println("Now watching game number " + params[0]);
                printG(out, false);
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid gameID");
            }
        }
        else {
            System.out.println("Not currently logged in.");
        }
    }
    public void help(){
        if (state == State.SIGNEDOUT) {
            System.out.println("help - display commands for logged in and logged out states");
            System.out.println("quit - end program");
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - create an account");
            System.out.println("login <USERNAME> <PASSWORD> - login to an existing account");
        } else {
            System.out.println("help - display commands for logged in and logged out states");
            System.out.println("logout - logout from program");
            System.out.println("create <GAMENAME> - create new game of specified name");
            System.out.println("list - list currently running games");
            System.out.println("join <GAMEID> <PLAYERCOLOR> - join a set game on a set player color");
            System.out.println("watch <GAMEID> - observe a game in progress from the sidelines");
        }
    }
    public void prompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + state +" >>>" + SET_TEXT_COLOR_GREEN);
    }
    private void printHorizontalBorder(PrintStream out, boolean black) {
        drawSquare(out, null, null, null);
        if (black) {
            for (int i = 7; i>=0; i--) {
                drawSquare(out, null, null, (char)('A' + i));
            }
        } else {
            for (int i = 0; i<8; i++) {
                drawSquare(out, null, null, (char)('A' + i));
            }
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
        if (black) { //Ascends from 1
            printHorizontalBorder(out, true);
            for (int boardRow = 0; boardRow < 8; ++boardRow) {
                drawSquare(out, null, null, (char) (boardRow + 49));
                //Using ascii interpretation, int converted to char of set number
                for (int boardCol = 7; boardCol >= 0; --boardCol) {
                    makeTile(out, boardRow, boardCol);
                }
                drawSquare(out, null, null, (char) (boardRow + 49));
                out.println(RESET_BG_COLOR);
            }
            printHorizontalBorder(out, true);
        } else{ //Descends from 8
            printHorizontalBorder(out, false);
            for (int boardRow = 7; boardRow >= 0; --boardRow) {
                drawSquare(out, null, null, (char) (boardRow + 49));
                //Using ascii interpretation, int converted to char of set number
                for (int boardCol = 0; boardCol < 8; ++boardCol) {
                    makeTile(out, boardRow, boardCol);
                }
                drawSquare(out, null, null, (char) (boardRow + 49));
                out.println(RESET_BG_COLOR);
            }
            printHorizontalBorder(out, false);
        }
    }

    private void makeTile(PrintStream out, int boardRow, int boardCol) {
        Color pieceColor = convertCol(board.getPiece(boardRow + 1, boardCol + 1));
        char piece = convert(board.getPiece(boardRow + 1, boardCol + 1));
        if ((boardRow + boardCol) % 2 == 0) {
            drawSquare(out, Color.black, pieceColor, piece);
        } else {
            drawSquare(out, Color.white, pieceColor, piece);
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

    @Override
    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.toString()); //Originally meant to be message, not toString
        prompt();
    }
}