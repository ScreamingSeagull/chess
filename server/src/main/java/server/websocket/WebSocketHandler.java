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
                Connection badConnection = new Connection("Unauthorized", session);
                badConnection.send(new Gson().toJson(new ErrorMessage("Error: Game not found.")));
            }
            Connection connection = new Connection(user.username(), session);
            switch (command.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> connectGame(connection, new Gson().fromJson(s, Connect.class));
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(connection, new Gson().fromJson(s, MakeMove.class));
                case UserGameCommand.CommandType.BOARDREQ -> getBoard(connection, new Gson().fromJson(s, BoardReq.class));
                case UserGameCommand.CommandType.RESIGN -> resignGame(connection, new Gson().fromJson(s, Resign.class));
                case UserGameCommand.CommandType.LEAVE -> leaveGame(connection, new Gson().fromJson(s, Leave.class));
            }
        } catch (WebSocketException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clear() {
        lobbies.clear();
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
    private void checkStatus(GameData gameData) throws IOException {
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
            lobbies.get(gameData.gameID()).endGame();
            lobbies.get(gameData.gameID()).notify("", new Notification("White has won."));
        }
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
            lobbies.get(gameData.gameID()).endGame();
            lobbies.get(gameData.gameID()).notify("", new Notification("Black has won."));
        }
    }
    private void getBoard(Connection connection, BoardReq message) throws WebSocketException {
        try {
            GameData game = gameDAO.getGame(message.getGameID());
            if (game == null) {
                connection.send(new Gson().toJson(new ErrorMessage("Error: Game not found.")));
            }else{
                connection.send(new Gson().toJson(new LoadGame(game)));
            }
        } catch (DataAccessException | IOException e) {
            throw new WebSocketException(e.getMessage());
        }
    }
    private void makeMove(Connection connection, MakeMove message) throws WebSocketException, IOException {
        try {
            GameData game = gameDAO.getGame(message.getGameID());
            ChessPiece piece = game.game().getBoard().getPiece(message.getMove().getStartPosition());
            if (lobbies.get(message.getGameID()).isEnded()){
                connection.send(new Gson().toJson(new ErrorMessage("Error: Game is over.")));
            }
            else if (piece == null ||
                    !connection.username.equals(game.whiteUsername()) && piece.getTeamColor()== ChessGame.TeamColor.WHITE ||
                    !connection.username.equals(game.blackUsername()) && piece.getTeamColor()== ChessGame.TeamColor.BLACK ){
                connection.send(new Gson().toJson(new ErrorMessage("Error: Invalid piece.")));
            } else if (piece.getTeamColor() != game.game().getTeamTurn()){
                connection.send(new Gson().toJson(new ErrorMessage("Error: Not your turn.")));
            }
            else{
                if (!lobbies.get(message.getGameID()).isEnded()) {
                    game.game().makeMove(message.getMove());
                    ChessGame.TeamColor enemy;
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        enemy = ChessGame.TeamColor.BLACK;
                    } else{
                        enemy = ChessGame.TeamColor.WHITE;
                    }
                    if(game.game().isInCheckmate(enemy)){
                        lobbies.get(game.gameID()).endGame();
                    }
                    gameDAO.updateGame(game.gameID(), game);
                    lobbies.get(message.getGameID()).notify("", new LoadGame(game));
                    lobbies.get(message.getGameID()).notify(connection.username, new Notification(String.format("%s moved %s from %s to %s.",
                            connection.username, piece.getPieceType().name(),
                            message.getMove().getStartPosition().toString(),
                            message.getMove().getEndPosition().toString())));
                    checkStatus(game);
                }
            }
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            connection.send(new Gson().toJson(new ErrorMessage("Error: Move is illegal.")));
        }
    }
    private void resignGame(Connection connection, Resign message) {
        try {
            GameData game = gameDAO.getGame(message.getGameID());
            if (connection.username.equals(game.whiteUsername()) || connection.username.equals(game.blackUsername())){
                if (!lobbies.get(message.getGameID()).isEnded()){
                    lobbies.get(message.getGameID()).notify(connection.username,
                            new Notification(String.format("%s has surrendered.", connection.username)));
                    connection.send(new Gson().toJson(new Notification("You have surrendered.")));
                    lobbies.get(message.getGameID()).endGame();
                } else {
                    connection.send(new Gson().toJson(new ErrorMessage("Error: Opponent already resigned.")));
                }
            } else{
                connection.send(new Gson().toJson(new ErrorMessage("Error: Cannot resign as spectator.")));
            }
        } catch (IOException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private void leaveGame(Connection connection, Leave message) throws WebSocketException {
        try{
            GameData game = gameDAO.getGame(message.getGameID());
            AuthData user = authDAO.getAuth(message.getAuthToken());
            GameData update;
            if (connection.username.equals(game.whiteUsername())){
                update = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                gameDAO.updateGame(game.gameID(), update);
            } else if (connection.username.equals(game.blackUsername())){
                update = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                gameDAO.updateGame(game.gameID(), update);
            }
            lobbies.get(message.getGameID()).remove(connection.username);
            lobbies.get(message.getGameID()).notify(connection.username, new Notification(connection.username + " has left."));

        } catch (DataAccessException | IOException e) {
            throw new WebSocketException(e.getMessage());
        }
    }
}
