package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.result.RegisterResult;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        String initAuths = """
                CREATE TABLE IF NOT EXISTS users (
                username varchar(256) NOT NULL,
                password varchar(256) NOT NULL,
                email varchar(256) NOT NULL,
                PRIMARY KEY (username)
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

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clearU() throws DataAccessException {
        executeUpdate("TRUNCATE users;");
    }

    public RegisterResult createUser(UserData userData, AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";
        String hash = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        executeUpdate(statement, userData.username(), hash, userData.email());
        return new RegisterResult(authData.authToken(), userData.username());
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?;";
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setString(1, username);
            try (var ns = ps.executeQuery()) {
                if (ns.next()) {
                    return new UserData(ns.getString("username"), ns.getString("password"), ns.getString("email"));
                }
                return null;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}