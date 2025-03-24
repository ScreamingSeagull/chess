package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String initAuths = """
                CREATE TABLE IF NOT EXISTS auths (
                username varchar(256) NOT NULL,
                authToken varchar(256) NOT NULL,
                PRIMARY KEY (authToken)
            );
            """;
        executeUpdate(initAuths);
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

    public void clearA() throws DataAccessException {
        executeUpdate("TRUNCATE auths;");
    }
    public void createAuth(AuthData data) throws DataAccessException {
        var statement = "INSERT INTO auths (username, authToken) VALUES (?, ?);";
        executeUpdate(statement, data.username(), data.authToken());
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, authToken FROM auths WHERE authToken=?;";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var newstate = ps.executeQuery()) {
                    if (newstate.next()) {
                        return new AuthData(newstate.getString("authToken"), newstate.getString("username"));
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void deleteAuth(AuthData authData) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?;";
        executeUpdate(statement, authData.authToken());
    }
}
