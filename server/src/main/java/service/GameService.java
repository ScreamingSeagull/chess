package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.result.*;
import model.request.*;

import java.util.Collection;

public class GameService {
    private UserDAO UDAO = new UserDAO();
    private AuthDAO ADAO = new AuthDAO();
    private GameDAO GDAO = new GameDAO();

    public GameService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        UDAO = udao;
        ADAO = adao;
        GDAO = gdao;
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        AuthData authData = ADAO.getAuth(authToken);
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        Collection<GameData> games = GDAO.listGames();
        return new ListGamesResult(games);
        //returns 500 if description
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData authData = ADAO.getAuth(createGameRequest.authToken());
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        return GDAO.createGame(createGameRequest.authToken());
        //returns 500 if description
    }
    public void joinGame(JoinGameRequest joinRequest, String authToken) throws DataAccessException {
        GameData gameData = GDAO.getGame(joinRequest.ID());
        if (gameData == null) {
            throw new DataAccessException("Error: bad request");
        }
        AuthData authData = ADAO.getAuth(authToken);
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        if (joinRequest.color().equals("WHITE") || joinRequest.color().equals("White") || joinRequest.color().equals("white")) {
            if(gameData.whiteUsername()!=null) {
                throw new ServiceException(403, "Error: already taken");
            }
        }
        if (joinRequest.color().equals("BLACK") || joinRequest.color().equals("Black") || joinRequest.color().equals("black")) {
            if(gameData.blackUsername()!=null) {
                throw new ServiceException(403, "Error: already taken");
            }
        }
        String username = authData.username();
        //Update the game itself with the username and then switch a string?

        //returns 500 if description
    }
}
