package server.websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import websocket.commands.*;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ErrorMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final ConcurrentHashMap<Integer, ConnectionManagement> lobbies = new ConcurrentHashMap<>();
    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String s) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(s, UserGameCommand.class);
        AuthData user = authDAO.getAuth(command.getAuthToken());
        try {
            if (user == null) {
                throw new WebSocketException("Error: unauthorized.");
            }
            Connection connection = new Connection(user.username(), session);
            switch (command.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> connectGame(connection, new Gson().fromJson(s, Connect.class));
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(connection, new Gson().fromJson(s, MakeMove.class));
                case UserGameCommand.CommandType.RESIGN -> resignGame(connection, new Gson().fromJson(s, Resign.class));
                case UserGameCommand.CommandType.LEAVE -> leaveGame(connection, new Gson().fromJson(s, Leave.class));
            }
        } catch (WebSocketException e) {
            throw new RuntimeException(e);
        }
    }
    private void lobbySetup(int gameID, Connection connection) {
        if (!lobbies.containsKey(gameID)) {
            lobbies.put(gameID, new ConnectionManagement());
        }
        lobbies.get(gameID).add(connection);
    }
    private void connectGame(Connection connection, Connect message) throws WebSocketException {
        try {
            GameData game = gameDAO.getGame(message.getGameID());
            if (game == null) {
                connection.send(new Gson().toJson(new ErrorMessage("Error: Game not found.")));
            }else{
                lobbySetup(message.getGameID(), connection);
                lobbies.get(message.getGameID()).notify(connection.username,
                        new Notification(connection.username + " has connected."));
                connection.send(new Gson().toJson(new LoadGame(game)));
            }
        } catch (DataAccessException | IOException e) {
            throw new WebSocketException(e.getMessage());
        }
    }
    private void ensureTeams(GameData gameData, String username, String teamColor) throws WebSocketException {
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
            throw new WebSocketException("Game is over!");
        }
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
            throw new WebSocketException("Game is over!");
        }
        if (teamColor == null){
            throw new WebSocketException("No team color given.");
        }
        if (teamColor.equals("white") && !gameData.whiteUsername().equals(username)){
            throw new WebSocketException("Spot taken");
        }
        if (teamColor.equals("black") && !gameData.blackUsername().equals(username)) {
            throw new WebSocketException("Spot taken");
        }
    }
    private void makeMove(Connection connection, MakeMove message) throws WebSocketException {
        try {
            boolean over = false;
            GameData game = gameDAO.getGame(message.getGameID());
            ChessPiece piece = game.game().getBoard().getPiece(message.getMove().getStartPosition());
            if (piece == null) {
                throw new WebSocketException("No piece selected.");
            }
            ensureTeams(game, connection.username, piece.getTeamColorString());
            game.game().makeMove(message.getMove());
            ChessGame.TeamColor enemy;
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                 enemy = ChessGame.TeamColor.BLACK;
            } else{
                enemy = ChessGame.TeamColor.WHITE;
            }
            if(game.game().isInCheckmate(enemy) || game.game().isInCheckmate(enemy)){
                //end game
                over = true;
            }
            gameDAO.updateGame(game.gameID(), game);
            lobbies.get(message.getGameID()).notify("", new LoadGame(game));
            lobbies.get(message.getGameID()).notify(connection.username, new Notification(String.format("%s moved %s from %s to %s.",
                    connection.username, piece.getPieceType().name(),
                    message.getMove().getStartPosition().toString(),
                    message.getMove().getEndPosition().toString())));
            if (over) {
                lobbies.get(message.getGameID()).notify("", new Notification(
                        String.format("%s has won.", connection.username)
                ));
            }

        } catch (DataAccessException | InvalidMoveException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void resignGame(Connection connection, Resign message) {

    }
    private void leaveGame(Connection connection, Leave message) {

    }
}
