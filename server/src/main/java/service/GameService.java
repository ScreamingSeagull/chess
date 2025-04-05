package service;
import dataaccess.*;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.result.*;
import model.request.*;

import java.util.Collection;

public class GameService {
    private UserDAO udao;
    private AuthDAO adao;
    private GameDAO gdao;

    public GameService(UserDAO udao, AuthDAO adao, GameDAO gdao) {
        this.udao = udao;
        this.adao = adao;
        this.gdao = gdao;
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        AuthData authData = adao.getAuth(authToken);
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        Collection<GameData> games = gdao.listGames();
        return new ListGamesResult(games);
        //returns 500 if description
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException, ServiceException {
        AuthData authData = adao.getAuth(authToken);
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        return gdao.createGame(createGameRequest.gameName());
        //returns 500 if description
    }
    public void joinGame(JoinGameRequest joinRequest, String authToken) throws DataAccessException, ServiceException {
        GameData gameData = gdao.getGame(joinRequest.gameID());
        GameData updatedGame;
        if (gameData == null || joinRequest.playerColor() == null || joinRequest.playerColor().isEmpty()) {
            throw new ServiceException(400, "Error: bad request");
        }
        AuthData authData = adao.getAuth(authToken);
        if (authData == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        if (joinRequest.playerColor().equals("WHITE") || joinRequest.playerColor().equals("White") || joinRequest.playerColor().equals("white")) {
            if (gameData.whiteUsername() != null){
                throw new ServiceException(403, "Error: already taken");
            }
            updatedGame = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());

        }
        else if(joinRequest.playerColor().equals("BLACK")||joinRequest.playerColor().equals("Black")||joinRequest.playerColor().equals("black")){
            if (gameData.blackUsername() != null){
                throw new ServiceException(403, "Error: already taken");
            }
            updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
        }
        else {
            throw new ServiceException(400, "Error: bad request");
        }
        gdao.updateGame(gameData.gameID(), updatedGame);
        //returns 500 if description
    }
}
