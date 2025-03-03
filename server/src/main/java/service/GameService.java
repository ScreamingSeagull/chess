package service;
import model.result.*;
import model.request.*;

public class GameService {
    public ListGamesResult listGames() {
        //return 200; //+ games list
        //Returns 401 if unauthorized, returns 500 if description
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        //Returns 400 if bad request, 401 if unauthorized, returns 500 if description
    }
    public void joinGame(JoinGameRequest joinRequest) {
        //Returns 401 if unauthorized, 403 if already taken, returns 500 if description
    }
}
