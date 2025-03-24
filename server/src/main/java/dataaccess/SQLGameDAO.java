package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.result.CreateGameResult;
import model.result.RegisterResult;

import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
    private int executeUpdate(String statement) throws DataAccessException {
        try (var con = DatabaseManager.getConnection()) {
            try (var update = con.prepareStatement(statement)) {
                update.executeUpdate();
            }
            return 0;
        } catch (SQLException e) {
            throw new DataAccessException("Not able to connect to database, SQL_User_DAO");
        }
    }

    public void clearG() throws DataAccessException {
        executeUpdate("TRUNCATE games;");
    }
    public CreateGameResult createGame(String name) throws DataAccessException {
//        ChessGame newGame = new ChessGame();
//        int ID = games.size() + 1;
//        GameData game = new GameData(ID, null, null, name, newGame);
//        var statement = "INSERT INTO games (ID, game) VALUES (?, ?)";
//        var id = executeUpdate(statement, ID, game);
//        return new CreateGameResult(ID);
        return null;
    }
    public GameData getGame(int id) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT id, json FROM games WHERE id=?";
//            return GameData;
//        }
        return null;
    }
    public Collection<GameData> listGames() throws DataAccessException {
//        return games.values();
         return null;
    }
    public void updateGame(int ID, GameData newGame) throws DataAccessException {
//        games.replace(ID, newGame);
    }
}
