package dataaccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.result.*;
import service.ServiceException;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void clearG() {
        games.clear();
        //returns 500 if description
    }
    public CreateGameResult createGame(String name) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        int ID = games.size() + 1;
        GameData game = new GameData(ID, null, null, name, newGame);
        games.put(ID, game);
        return new CreateGameResult(ID);
    }
    public GameData getGame(int id) throws DataAccessException {
        return games.get(id);
    }
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }
    public void updateGame(int ID, GameData newGame) throws DataAccessException {
        games.replace(ID, newGame);
    }
}