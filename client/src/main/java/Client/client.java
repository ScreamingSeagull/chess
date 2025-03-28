package Client;

import chess.*;
import exception.ResponseException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import static ui.EscapeSequences.*;

public class client {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    public client(String domainName) throws URISyntaxException, IOException {
        server = new ServerFacade(domainName);
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
        server.clearDB();
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
    private void prompt() {
        System.out.println("\n" + RESET_TEXT_COLOR + ">>>" + SET_TEXT_COLOR_GREEN);
    }
    private void printGame() {
        stringBoard(null, null, false);
        prompt();
    }
    private String stringBoard(ChessBoard board, Collection<ChessPosition> locations, boolean black){
        int up = black ? 1:8;
        int down = black ? 8:1;
        int diff = black ? -1:1;
        StringBuilder sb = new StringBuilder();
        boolean alt = true;
        buildUp(sb, black);
        boolean altTile = true; //switch between the two each time, boolean = !boolean
        buildTile(sb, altTile, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN, true);
        sb.append(RESET_BG_COLOR + '\n');
        return "null";
    }
    private void buildUp(StringBuilder sb, boolean black) {
        int up = black ? 1:8;
        int down = black ? 8:1;
        char diff = (char) (black ? -1:1);
        buildBorder(sb);
    }
    private void buildBorder(StringBuilder sb) {
        sb.append(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_WHITE);
        sb.append("dimensions");
        sb.append("*");
        sb.append("dimensions");
    }
    private void buildTile(StringBuilder sb, boolean altColor, ChessGame.TeamColor teamColor, ChessPiece.PieceType piece, boolean selected) {
        if (selected) {
            sb.append("selected");
        } else if (altColor) {
            sb.append("altcolor");
        } else {
            sb.append("basic color");
        }
        if (teamColor == ChessGame.TeamColor.WHITE) {
            sb.append(SET_TEXT_COLOR_WHITE);
        } else {
            sb.append(SET_TEXT_COLOR_BLACK);
        }
    }
    private void buildPiece(StringBuilder sb, ChessGame.TeamColor teamColor, ChessPiece.PieceType piece) { //May need config options for printing?
        if (piece != null) {
            switch(piece) {
                case KING:
                    break;
                case QUEEN:
                    break;
                case BISHOP:
                    break;
                case ROOK:
                    break;
                case KNIGHT:
                    break;
                case PAWN:
                    break;
            }
        } else {
            sb.append("n/a");
        }
    }
}
