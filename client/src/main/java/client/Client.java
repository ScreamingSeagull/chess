package client;

import chess.*;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ListGamesResult;
import websocket.messages.*;
import javax.websocket.MessageHandler;
import java.nio.charset.StandardCharsets;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client implements MessageHandler.Whole<String> {
    private final ServerFacade serverFacade;
    private State state = State.SIGNEDOUT;
    private State game = State.SIGNEDOUT; //used to see if in a  game
    private int gameID = -1;
    private boolean black = false;
    private boolean highlight = false;
    private ChessPosition highlightSpot;
    Collection<ChessMove> highlights;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    ChessBoard board = new ChessBoard();
    ArrayList<GameData> gameDataArrayList = new ArrayList<>();
    public Client(String serverUrl) throws URISyntaxException, IOException {
        serverFacade = new ServerFacade(serverUrl, this);
        board.resetBoard();
    }
    public void eval(String input) {
        String[] line = input.toLowerCase().split(" ");
        String cmd;
        if (line.length == 0) {
            cmd = "help";
        } else {
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
                case "watch" ->observe(params);
                case "move" ->makeMove(params);
                case "highlight" -> highlight(params);
                case "redraw" -> redraw();
                case "resign" ->resign();
                case "leave" -> leaveGame();
                case "quit" ->quitLogout();
                default -> System.out.println("Incorrect command entered.");
            }
        } catch (ResponseException e) {
            out.println(e.getMessage()); //999 error code because idk what the code is supposed to be
        }
    }
    public void clearDB() {
        serverFacade.clearDB();
        System.out.println("All databases wiped.");
        state = State.SIGNEDOUT;
        game = State.SIGNEDOUT;
        gameID = -1;
    }
    public void register(String... params) {
        if (state.equals(State.SIGNEDOUT)){
            if (params.length != 3){
                System.out.println("Please provide Username, Password, and Email.");
            } else{
                RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);
                serverFacade.register(req);
                System.out.println("Successfully registered.");
                state = State.SIGNEDIN;
            }
        } else {
            System.out.println("Already logged in.");
        }
    }
    public void login(String... params) {
        if (state.equals(State.SIGNEDOUT)){
            if(params.length!=2){
                System.out.println("Please provide a username and password.");
            } else{
                LoginRequest req = new LoginRequest(params[0], params[1]);
                serverFacade.login(req);
                state = State.SIGNEDIN;
                System.out.println("Logged in.");
            }
        } else {
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
            if(game == State.SIGNEDIN){
                leaveGame();
            }
            serverFacade.logout();
            state = State.SIGNEDOUT;
            System.out.println("Logged out.");
        } else {
            System.out.println("Not currently logged in, cannot log out.");
        }
    }
    public void createList(){
        ListGamesResult res = serverFacade.list();
        gameDataArrayList.clear();
        gameDataArrayList.addAll(res.games());
    }
    public void list(){ //Actually display the listed games
        if(state == state.SIGNEDIN) {
            createList();
            if (gameDataArrayList.isEmpty()) {
                System.out.println("No current games.");
            } else {
                System.out.println("All games:"); //Converts between game ID and number in list
                for (int i = 0; i < gameDataArrayList.size(); i++) {
                    System.out.println("Chess game " + (i+1) + ": " + gameDataArrayList.get(i));
                }
            }
        } else {
            System.out.println("Not signed in.");
        }
    }
    public void create(String... params){
        if(state == state.SIGNEDIN) {
            if(params.length != 1) {
                System.out.println("Only enter the name of the game you wish to create.");
            } else{
                serverFacade.create(params[0]);
                System.out.println("Created new chess game " + params[0]);
            }
        } else {
            System.out.println("Not currently logged in.");
        }
    }
    public void join(String... params){
        if(state == state.SIGNEDIN) {
            if(params.length != 2) {
                System.out.println("Please input only Game ID and player color.");
            } else if (params[1].equals("white") || params[1].equals("black")) {
                if (params[1].equals("white")) {black = false;} //Remember to set back to false when leaving
                else {black = true;}
                try {
                    if (Integer.parseInt(params[0]) > gameDataArrayList.size() || Integer.parseInt(params[0]) - 1 < 0) {
                        System.out.println("Invalid gameID.");
                    } else {
                        joinGame(params);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please input a valid gameID");
                }
            } else {System.out.println("Please input a valid player color.");}
        } else {
            System.out.println("Not currently logged in.");
        }
    }
    private void joinGame(String[] params) {
        gameID = (gameDataArrayList.get(Integer.parseInt(params[0]) - 1).gameID());
        JoinGameRequest req = new JoinGameRequest(gameID, params[1]);
        serverFacade.join(req);
        System.out.println("Joined game.");
        game = State.SIGNEDIN;
    }
    public void observe(String... params) {
        if(state == state.SIGNEDIN) {
            black = false;
            if(params.length != 1) {
                System.out.println("Please input only Game ID");
            }
            try {
                gameID = Integer.parseInt(params[0]);
                System.out.println("Now watching game number " + params[0]);
                serverFacade.observeGame(gameID);
                game = State.SIGNEDIN;
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid gameID");
            }
        } else {
            System.out.println("Not currently logged in.");
        }
    }
    public void makeMove(String... params) {
        if(state == state.SIGNEDIN) {
            if(game == state.SIGNEDIN){
                ChessMove move = getChessMove(params);
                if (move != null){
                    serverFacade.chessMove(gameID, move);
                }
            } else {
                System.out.println("Not currently in a game.");
            }
        } else {
            System.out.println("Not currently logged in.");
        }
    }
    private static ChessMove getChessMove(String[] params) {
        if (params.length == 4 || params.length == 5) {
            int startRow;
            int startCol;
            int endRow;
            int endCol;
            try {
                startRow = Integer.parseInt(params[0]);
                startCol = (params[1].charAt(0) - 'a' + 1);
                endRow = Integer.parseInt(params[2]);
                endCol = (params[3].charAt(0) - 'a' + 1);
            } catch (Exception e) {
                System.out.println("Please input valid coordinates.");
                return null;
            }
            if (startRow > 8 || startRow < 1 || startCol > 8 || startCol < 1) {
                System.out.println("Invalid starting position.");
                return null;
            } else if (endRow > 8 || endRow < 1 || endCol > 8 || endCol < 1) {
                System.out.println("Invalid end position.");
                return null;
            } else {
                ChessPosition start = new ChessPosition(startRow, startCol);
                ChessPosition end = new ChessPosition(endRow, endCol);
                ChessPiece.PieceType type;
                if (params.length == 5){
                    switch (params[4]){
                        case "knight"->type = ChessPiece.PieceType.KNIGHT;
                        case "bishop"->type = ChessPiece.PieceType.BISHOP;
                        case "rook"->type = ChessPiece.PieceType.ROOK;
                        case "queen"->type = ChessPiece.PieceType.QUEEN;
                        default -> type = null;
                    }
                } else {
                    type = null;
                }
                return new ChessMove(start, end, type);
            }
        }else {
            System.out.println("Please input a chess move: " +
                    "starting row and column ending row and column and promotion type " +
                    "(none if not available).");
            return null;
        }
    }
    public void resign(){
        System.out.println("Are you sure you want to resign?");
        Scanner scan = new Scanner(System.in);
        String line = "";
        prompt();
        line = scan.nextLine();
        if (line.equals("yes")){
            if(state == state.SIGNEDIN) {
                if(game == state.SIGNEDIN){
                    serverFacade.resign(gameID);
                } else {
                    System.out.println("Not currently in a game.");
                }
            } else {
                System.out.println("Not currently logged in.");
            }
        }
    }
    public void leaveGame(){
        if(state == state.SIGNEDIN) {
            if(game == state.SIGNEDIN){
                serverFacade.leaveGame(gameID);
                game = state.SIGNEDOUT;
                gameID=-1;
            } else {
                System.out.println("Not currently in a game.");
            }
        } else {
            System.out.println("Not currently logged in.");
        }
    }
    public void help(){
        if (state == State.SIGNEDOUT) {
            System.out.println("help - display commands for logged in and logged out states");
            System.out.println("quit - end program");
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - create an account");
            System.out.println("login <USERNAME> <PASSWORD> - login to an existing account");
        } else if (state == State.SIGNEDIN && game == State.SIGNEDOUT){
            System.out.println("help - display commands for logged in and logged out states");
            System.out.println("logout - logout from program");
            System.out.println("create <GAMENAME> - create new game of specified name");
            System.out.println("list - list currently running games");
            System.out.println("join <GAMEID> <PLAYERCOLOR> - join a set game on a set player color");
            System.out.println("watch <GAMEID> - observe a game in progress from the sidelines");
        } else if (state == State.SIGNEDIN && game == State.SIGNEDIN){
            System.out.println("help - display commands for logged in and logged out states");
            System.out.println("move - <starting row> <starting column> <ending row> " +
                    "<ending column> <promotion type, only for end> - move piece from start to end");
            System.out.println("highlight - <row> <column> - display legal moves for a piece");
            System.out.println("redraw - redraw the chessboard");
            System.out.println("resign - surrender the game to the opponent");
            System.out.println("leave - leave the game");
        }
    }
    public void prompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + state +" >>>" + SET_TEXT_COLOR_GREEN);
    } public void redraw(){
        if(game == State.SIGNEDIN){
            serverFacade.boardReq(gameID);
        } else {
            System.out.println("You are not currently in a game.");
        }
    }
    public void highlight(String... params){
        if (params.length == 2) {
            try {
                int startRow = Integer.parseInt(params[0]);
                int startCol = (params[1].charAt(0) - 'a' + 1);
                if (startRow > 8 || startRow < 1 || startCol > 8 || startCol < 1) {
                    System.out.println("Invalid starting position.");
                }
                highlight = true;
                highlightSpot = new ChessPosition(startRow, startCol);
                serverFacade.boardReq(gameID);
            } catch (Exception e) {
                System.out.println("Please input valid coordinates");
            }
        }
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
            case KING: return 'K';
            case QUEEN: return 'q';
            case ROOK: return 'r';
            case BISHOP: return 'b';
            case KNIGHT: return 'k';
            case PAWN: return 'p';
            default: return ' ';
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
    private void printG(PrintStream out, boolean black, GameData game) {
        if (black) { //Ascends from 1
            printHorizontalBorder(out, true);
            for (int boardRow = 0; boardRow < 8; ++boardRow) {
                drawSquare(out, null, null, (char) (boardRow + 49));
                //Using ascii interpretation, int converted to char of set number
                for (int boardCol = 7; boardCol >= 0; --boardCol) {
                    makeTile(out, boardRow, boardCol, game);
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
                    makeTile(out, boardRow, boardCol, game);
                }
                drawSquare(out, null, null, (char) (boardRow + 49));
                out.println(RESET_BG_COLOR);
            }
            printHorizontalBorder(out, false);
        }
    }
    private void makeTile(PrintStream out, int boardRow, int boardCol, GameData game){
        if(game == null) {
            tileCreate(out, boardRow, boardCol, board);
        } else {
            tileCreate(out, boardRow, boardCol, game.game().getBoard());
        }
    }
    private void tileCreate(PrintStream out, int boardRow, int boardCol, ChessBoard chessBoard) {
        Color pieceColor = convertCol(chessBoard.getPiece(boardRow + 1, boardCol + 1));
        char piece = convert(chessBoard.getPiece(boardRow + 1, boardCol + 1));
        if (highlight && highlights != null){
            ChessPosition temp = new ChessPosition(boardRow, boardCol);
            Collection<ChessPosition> spotTemp = new ArrayList<>();
            for (ChessMove move : highlights){
                ChessPosition tempPos = new ChessPosition(move.getEndPosition().getRow()-1,
                        move.getEndPosition().getColumn()-1);
                spotTemp.add(tempPos);
            }
            if(spotTemp.contains(temp)){
                drawHighlight(out, pieceColor, piece);
            } else{
                checkerPattern(out, boardRow, boardCol, pieceColor, piece);
            }
        } else{
            checkerPattern(out, boardRow, boardCol, pieceColor, piece);
        }
    }
    private void checkerPattern(PrintStream out, int boardRow, int boardCol, Color pieceColor, char piece) {
        if ((boardRow + boardCol) % 2 == 0) {
            drawSquare(out, Color.black, pieceColor, piece);
        } else {
            drawSquare(out, Color.white, pieceColor, piece);
        }
    }
    private void drawHighlight(PrintStream out, Color pieceColor, Character c){
        out.print(SET_BG_COLOR_BLUE);
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
    public void onMessage(String s) {
        ServerMessage msg = new Gson().fromJson(s, ServerMessage.class);
        switch (msg.getServerMessageType()) {
            case NOTIFICATION: {
                printNotification(new Gson().fromJson(s, Notification.class));
                break;
            }
            case LOAD_GAME: {
                LoadGame loadGame = new Gson().fromJson(s, LoadGame.class);
                if (highlight){
                    highlights = loadGame.getGame().game().validMoves(highlightSpot);
                }
                printGame(loadGame);
                highlight = false;
                highlightSpot = null;
                highlights.clear();
                break;
            }
            case ERROR: {
                printError(new Gson().fromJson(s, ErrorMessage.class));
                break;
            }
        }
    }
    public void printNotification(Notification message) {
        out.print(SET_TEXT_COLOR_BLUE + message.getMessage());
        prompt();
    }
    public void printGame(LoadGame message) {
        System.out.println("\n");
        printG(out, black, message.getGame());
        prompt();
    }
    public void printError(ErrorMessage message){
        out.print(SET_TEXT_COLOR_RED + message.getMessage());
        prompt();
    }
}