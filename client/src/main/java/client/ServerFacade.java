package client;
import chess.ChessMove;
import client.http.HttpComms;
import client.websocket.WebSocketComms;
import com.google.gson.Gson;
import exception.ResponseException;
import model.request.*;
import model.result.*;
import websocket.commands.Connect;
import websocket.commands.Observe;

import javax.websocket.MessageHandler;
import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacade {
    private final String serverUrl;
    private final MessageHandler.Whole<String> messageHandler;
    private WebSocketComms webSocketComms;
    private final HttpComms httpComms;
    private String authToken;

    public ServerFacade(String serverUrl, MessageHandler.Whole<String> messageHandler) throws URISyntaxException, IOException {
        this.serverUrl = serverUrl;
        this.messageHandler = messageHandler;
        httpComms = new HttpComms(serverUrl);
    }
    public void clearDB() {
        httpComms.clearDB();
    }
    public RegisterResult register(RegisterRequest req) throws ResponseException {
        RegisterResult res = httpComms.register(req);
        authToken = res.authToken();
        webSocketComms = new WebSocketComms(serverUrl, messageHandler);
        return res;
    }
    public LoginResult login(LoginRequest req) {
        LoginResult res = httpComms.login(req);
        authToken = res.authToken();
        webSocketComms = new WebSocketComms(serverUrl, messageHandler);
        return res;
    }
    public void logout() {
        httpComms.logout(authToken);
        webSocketComms = null;
        authToken = null;
    }
    public ListGamesResult list() {
        return httpComms.list(authToken);
    }
    public CreateGameResult create(String name) {
        return httpComms.create(name, authToken);
    }
    public void join(JoinGameRequest req) {
        httpComms.join(req, authToken);
        webSocketComms.sendMessage(new Connect(req.gameID(), req.playerColor(), authToken));
    }
    public void observeGame(int gameID){
        webSocketComms.sendMessage(new Observe(gameID, authToken));
    }
    public void chessMove(int gameID, ChessMove move) {

    }
    public void resign(int gameID) { //DOES NOT LEAVE GAME

    }
    public void leaveGame(int gameID){

    }
}
