package server;

import dataaccess.*;
import model.AuthData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.Gson;
import websocket.commands.*;
import websocket.commands.UserGameCommand;


@WebSocket
public class WebSocketHandler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    @OnWebSocketMessage public void onMessage(Session session, String s) throws DataAccessException {
        UserGameCommand command = new Gson().fromJson(s, UserGameCommand.class);
        AuthData user = authDAO.getAuth(command.getAuthToken());
        try {
            if (user == null) {
                throw new WebSocketException("Error: unauthorized.");
            }
            Connection connection = new Connection(user.username(), session);
            switch (command.getCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> joinGame(connection, new Gson().fromJson(s, Connect.class));
                case UserGameCommand.CommandType.OBSERVE -> watchGame(connection, new Gson().fromJson(s, Observe.class));
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(connection, new Gson().fromJson(s, MakeMove.class));
                case UserGameCommand.CommandType.RESIGN -> resignGame(connection, new Gson().fromJson(s, Resign.class));
                case UserGameCommand.CommandType.LEAVE -> leaveGame(connection, new Gson().fromJson(s, Leave.class));
            }
        } catch (WebSocketException e) {
            throw new RuntimeException(e);
        }
    }
    private void joinGame(Connection connection, Connect message) {

    }
    private void watchGame(Connection connection, Observe message) {

    }
    private void makeMove(Connection connection, MakeMove message) {

    }
    private void resignGame(Connection connection, Resign message) {

    }
    private void leaveGame(Connection connection, Leave message) {

    }
}
