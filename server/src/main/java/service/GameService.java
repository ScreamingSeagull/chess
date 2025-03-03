package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.result.*;
import model.request.*;

public class GameService {
    private GameDAO DAOG = new GameDAO();
    private AuthDAO DAOA = new AuthDAO();
    public ListGamesResult listGames(String authToken) throws DataAccessException {
        AuthData authData = DAOA.getAuth(authToken);
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        GameData[] games = DAOG.listGames();
        return new ListGamesResult(games);
        //returns 500 if description
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData authData = DAOA.getAuth(createGameRequest.authToken());
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        return DAOG.createGame();
        //returns 500 if description
    }
    public void joinGame(JoinGameRequest joinRequest, String authToken) throws DataAccessException {
        GameData gameData = DAOG.getGame(joinRequest.ID());
        if (gameData == null) {
            throw new DataAccessException("Error: bad request");
        }
        AuthData authData = DAOA.getAuth(authToken);
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
