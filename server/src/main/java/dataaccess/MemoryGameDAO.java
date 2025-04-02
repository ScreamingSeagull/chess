package dataaccess;
import model.GameData;
import model.result.*;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public void clearG() {
        games.clear();
        //returns 500 if description
    }
    public CreateGameResult createGame(String name) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        int id = games.size() + 1;
        GameData game = new GameData(id, null, null, name, newGame);
        games.put(id, game);
        return new CreateGameResult(id);
    }
    public GameData getGame(int id) throws DataAccessException {
        return games.get(id);
    }
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }
    public void updateGame(int id, GameData newGame) throws DataAccessException {
        games.replace(id, newGame);
    }
}