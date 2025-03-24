package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import model.GameData;
import model.result.CreateGameResult;
import model.result.RegisterResult;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String initGames = """
                CREATE TABLE IF NOT EXISTS games (
                gameID int NOT NULL AUTO_INCREMENT,
                gameName varchar(256) NOT NULL,
                whiteUsername varchar(256) NOT NULL,
                blackUsername varchar(256) NOT NULL,
                game longtext NOT NULL,
                PRIMARY KEY (gameID)
            );
            """;
        executeUpdate(initGames);
    }
    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var update = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String s) update.setString(i+1, s);
                    if (param instanceof ChessGame g) update.setString(i+1, new Gson().toJson(g));
                    if (param instanceof Boolean b) update.setBoolean(i+1, b);
                    if (param instanceof Integer j) update.setInt(i+1, j);
                    if (param == null) update.setNull(i+1, NULL);
                }
                update.executeUpdate();
                var keys = update.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Not able to connect to database, SQL_User_DAO");
        }
    }

    public void clearG() throws DataAccessException {
        executeUpdate("TRUNCATE games;");
    }
    public CreateGameResult createGame(String name) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        var statement = "INSERT INTO games (gameName, whiteUsername, blackUsername, game)) VALUES (?, ?, ?, ?)";
        int id = executeUpdate(statement, null, null, null, newGame);
        return new CreateGameResult(id);
    }
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var ns = ps.executeQuery()) {
                    if (ns.next()) {
                        return new GameData(ns.getInt("gameID"), ns.getString("whiteUsername"), ns.getString("blackUsername"), ns.getString("gameName"), new Gson().fromJson(ns.getString("game"), ChessGame.class));
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            ArrayList<GameData> games = new ArrayList<>();
            String statement = "SELECT * FROM games;";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                try (var ns = ps.executeQuery()) {
                    while (ns.next()) {
                        games.add(new GameData(ns.getInt("gameID"), ns.getString("whiteUsername"), ns.getString("blackUsername"), ns.getString("gameName"), new Gson().fromJson(ns.getString("game"), ChessGame.class)));
                    }

                }
            }
            return games;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void updateGame(int gameID, GameData newGame) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=?, WHERE gameID=?;";
        executeUpdate(statement, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName(), new Gson().toJson(newGame.game()), gameID);
    }
}
