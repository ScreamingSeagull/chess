package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.result.CreateGameResult;

import java.util.Collection;

public interface GameDAO {
    public void clearG() throws DataAccessException;
    public CreateGameResult createGame(String name) throws DataAccessException;
    public GameData getGame(int id) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(int id, GameData newGame) throws DataAccessException;
}
